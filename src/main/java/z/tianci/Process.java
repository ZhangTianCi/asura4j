package z.tianci;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Date;

public class Process {
    public static void exec(String[] args, StringBuilder output, StringBuilder error) {
        exec(args, output, error, -1L);
    }

    public static void exec(String[] args, StringBuilder output, StringBuilder error, Long overtime) {
        //待考虑
        boolean isOutputLog = output != null;    // 是否记录输出日志
        boolean isErrorLog = error != null;      // 是否记录错误日志
        try {
            final Date startTime = new Date();
            final java.lang.Process process = new ProcessBuilder(args).start();
            final BufferedReader commandResponse = new BufferedReader(new InputStreamReader(process.getInputStream(), "gbk"));
            final BufferedReader commandErrorResponse = new BufferedReader(new InputStreamReader(process.getErrorStream(), "gbk"));
            Thread outputThread = new Thread(() -> {
                String line;
                try {
                    while (isOutputLog && (line = commandResponse.readLine()) != null) {
                        output.append(line).append("\n");
                    }
                } catch (IOException e) {
                    output.append(e).append("\n");
                }
            });
            Thread errorThread = new Thread(() -> {
                String line;
                try {
                    while (isErrorLog && (line = commandErrorResponse.readLine()) != null) {
                        error.append(line).append("\n");
                    }
                } catch (IOException e) {
                    error.append(e).append("\n");
                }
            });
            //待考虑
            //process.waitFor(overtime, TimeUnit.MILLISECONDS);
            Thread overtimeThread = new Thread(() -> {
                while (overtime > 0) {
                    if (System.currentTimeMillis() - startTime.getTime() > overtime) {
                        if (isOutputLog) {
                            output.append("任务已超时").append("\n");
                        }
                        if (isErrorLog) {
                            error.append("任务超时被终止").append("\n");
                        }
                        process.destroy();
                        break;
                    }
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        if (isOutputLog) {
                            output.append("任务设置了超时时间 , 在时间内完成了任务 , 超时检测线程停止。").append("\n");
                        }
                    }
                }
            });
            outputThread.start();
            errorThread.start();
            overtimeThread.start();
            process.waitFor();
            overtimeThread.interrupt();
        } catch (Exception e) {
            if (isErrorLog) {
                error.append(e.getMessage()).append("\n");
            }
        } finally {
        }
        return;
    }
}
