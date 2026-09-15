package org.swiftboot.web.i18n;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.i18n.LocaleContext;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.context.i18n.SimpleLocaleContext;
import org.springframework.core.task.TaskDecorator;

import java.util.Locale;

/**
 * For getting correct locale in any Spring thread including:
 * @Async
 * CompletableFuture.runAsync() / supplyAsync()
 * ExecutorService / ThreadPoolTaskExecutor
 *
 * since the LocaleContextHolder can be visited outside the main thread.
 *
 * @since 3.2
 */
public class LocaleContextDecorator implements TaskDecorator {

    private static final Logger log = LoggerFactory.getLogger(LocaleContextDecorator.class);

    @Override
    public Runnable decorate(Runnable runnable) {
        // Get local from current thread.
        Locale locale = LocaleContextHolder.getLocale();
        log.debug("Wrap locale %s to new thread".formatted(locale));
        // wrap the context to avoid the HttpServletRequest be visited in thread(which causes exception)
        LocaleContext safeContext = new SimpleLocaleContext(locale);

        return () -> {
            try {
                // Set the locale context in new thread.
                LocaleContextHolder.setLocaleContext(safeContext, true);
                runnable.run();
            } finally {
                // !MUST release the context after using.
                LocaleContextHolder.resetLocaleContext();
            }
        };
    }
}
