package org.swiftboot.demo.controller;

import jakarta.servlet.AsyncContext;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.concurrent.CompletableFuture;

@Controller
@RequestMapping("/sse")
@ResponseBody
public class SseController {


    private static final Logger log = LoggerFactory.getLogger(SseController.class);

    /**
     * Test exception:
     * java.lang.IllegalStateException: The request object has been recycled and is no longer associated with this facade
     *
     * @param httpRequest
     * @param httpResponse
     */
    @GetMapping("facade")
    public void timeout(HttpServletRequest httpRequest, HttpServletResponse httpResponse) {
        // 1. 开启标准的 Servlet 异步（不使用 Spring 的 SseEmitter，脱离 Spring 保护圈）
        AsyncContext asyncContext = httpRequest.startAsync();

        // 2. 关键点：我们让主线程立刻“主动完成”这个异步上下文
        // 告诉 Tomcat：“我已经处理完了，你可以把 request 回收放回对象池了！”
        asyncContext.complete();

        // 3. 异步线程在 Tomcat 回收完之后，延迟去强行“刨坟”
        CompletableFuture.runAsync(() -> {
            try {
                // 故意等 1 秒，确保 Tomcat 已经把当前请求彻底 Recycle 了
                Thread.sleep(1000);

                // 4. 此时连接已死，Tomcat 的 Facade 壳子还在，但内部 Request 已经空了
                // 这一行 100% 抛出：java.lang.IllegalStateException: The request object has been recycled...
                String method = httpRequest.getMethod();
                System.out.println("Method: " + method);

            } catch (Exception e) {
                System.err.println("🔥 成功复现 Tomcat 底层错误：");
                e.printStackTrace();
            }
        });
    }

    @GetMapping("/get/{duration}")
    public SseEmitter sseGet(@PathVariable Integer duration, HttpServletRequest httpRequest, HttpServletResponse httpResponse) {
        log.debug("/sse/get");
        duration = duration == null ? 10 : duration;
        SseEmitter sseEmitter = new SseEmitter(5000L);
        int count = duration * 2;
        CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {

            for (int i = 0; i < count; i++) {
                try {
                    Thread.sleep(6000);
                    sseEmitter.send("chunk " + i);
//                    httpResponse.flushBuffer();
                    System.out.println(httpRequest.getHeader("Accept-Language"));
                    Thread.sleep(500);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        future.whenComplete((unused, throwable) -> {
            log.debug("Completed %s".formatted(throwable == null ? "" : throwable.getLocalizedMessage()));
            sseEmitter.complete();
        });
        return sseEmitter;
    }
}
