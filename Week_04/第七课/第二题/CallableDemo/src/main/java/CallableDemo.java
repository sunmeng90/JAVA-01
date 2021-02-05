import java.util.concurrent.*;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.LockSupport;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Supplier;

public class CallableDemo {

    static class Callable1 {

        private String val;

        public static void main(String[] args) throws InterruptedException {
            Callable1 c1 = new Callable1();
            new Thread(() -> c1.val = helloFn.get()).start();
            while (c1.val == null) {
                Thread.sleep(50); //TODO: why remove this line, the program will not exit
            } // blocking on not initialized
            System.out.println("return val: " + c1.val);
        }
    }

    static class Callable2 {

        public static void main(String[] args) throws ExecutionException, InterruptedException {
            FutureTask<String> task = new FutureTask<>(() -> helloFn.get());
            new Thread(task).start();
            System.out.println("return val: " + task.get());
        }

    }

    static class Callable3 {

        public static void main(String[] args) throws Exception {
            Callable<String> callable = () -> helloFn.get();
            ExecutorService executorService = Executors.newFixedThreadPool(1);
            Future<String> future = executorService.submit(callable);
            System.out.println("return val: " + future.get());
            executorService.shutdown();
        }

    }

    static class Callable4 {
        private String val;

        public static void main(String[] args) throws Exception {
            Callable4 c4 = new Callable4();
            Runnable runnable = () -> {
                c4.val = helloFn.get();
            };
            ExecutorService executorService = Executors.newFixedThreadPool(1);
            Future<?> future = executorService.submit(runnable);
            future.get(); // block by future
            System.out.println("return val: " + c4.val);
            executorService.shutdown();
        }

    }


    static class Callable5 {
        private String val;

        public static void main(String[] args) {
            Callable5 c5 = new Callable5();

            new Thread(() -> {
                synchronized (c5) {
                    c5.val = helloFn.get();
                    c5.notify();
                }
            }).start();

            new Thread(() -> {
                synchronized (c5) {
                    while (c5.val == null) {
                        try {
                            c5.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    System.out.println("return val: " + c5.val);
                }
            }).start();
        }
    }

    static class Callable6 {
        private String val;

        public static void main(String[] args) throws InterruptedException {
            CountDownLatch countDownLatch = new CountDownLatch(1);

            Callable6 c6 = new Callable6();

            new Thread(() -> {
                c6.val = helloFn.get();
                countDownLatch.countDown();
            }).start();

            countDownLatch.await();
            System.out.println("return val: " + c6.val);
        }

    }

    static class Callable7 {

        private String val;

        public static void main(String[] args) {
            Callable7 c7 = new Callable7();

            CyclicBarrier cyclicBarrier = new CyclicBarrier(1, () -> System.out.println("return val: " + c7.val));

            new Thread(() -> {
                c7.val = helloFn.get();
                try {
                    cyclicBarrier.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (BrokenBarrierException e) {
                    e.printStackTrace();
                }
            }).start();
        }
    }

    static class Callable8 {

        private String val;

        public static void main(String[] args) {
            Callable8 c8 = new Callable8();

            Thread t1 = new Thread(() -> {
                c8.val = helloFn.get();
            });

            Thread t2 = new Thread(() -> {
                try {
                    t1.join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("return val: " + c8.val);
            });

            t1.start();
            t2.start();
        }
    }

    static class Callable9 {

        private String val;

        public static void main(String[] args) {
            Callable9 c9 = new Callable9();
            ExecutorService executorService = Executors.newSingleThreadExecutor(); // execution in order
            executorService.submit(() -> {
                c9.val = helloFn.get();
            });
            executorService.submit(() -> {
                System.out.println("return val: " + c9.val);
            });

            executorService.shutdown();
        }
    }

    static class Callable10 {

        public static void main(String[] args) {
            CompletableFuture.supplyAsync(helloFn)
                    .thenAccept(val -> System.out.println("return val: " + val));
        }

    }

    static class Callable11 {
        private String val;

        public static void main(String[] args) {
            Callable11 c11 = new Callable11();
            ReentrantLock lock = new ReentrantLock();
            Condition completed = lock.newCondition();

            new Thread(() -> {
                lock.lock();
                try {
                    c11.val = helloFn.get();
                    completed.signal();
                } finally {
                    lock.unlock();
                }
            }).start();

            new Thread(() -> {
                lock.lock();
                try {
                    if (c11.val == null) {
                        completed.await();
                    }
                    System.out.println("return val: " + c11.val);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    lock.unlock();
                }

            }).start();

        }

    }

    static class Callable12 {

        private String val;

        public static void main(String[] args) {
            Callable12 c12 = new Callable12();

            Thread t2 = new Thread(() -> {
                if (c12.val == null) {
                    LockSupport.park();
                }
                System.out.println("return val: " + c12.val);
            });

            Thread t1 = new Thread(() -> {
                c12.val = helloFn.get();
                LockSupport.unpark(t2);
            });

            t1.start();
            t2.start();
        }
    }

    static class Callable13 {

        private String val;

        public static void main(String[] args) {
            Callable13 c13 = new Callable13();

            Thread t2 = new Thread(() -> {
                if (c13.val == null) {
                    LockSupport.park();
                }
                System.out.println("return val: " + c13.val);
            });

            Thread t1 = new Thread(() -> {
                c13.val = helloFn.get();
                t2.interrupt(); // interrupt
            });

            t1.start();
            t2.start();
        }
    }


    static class Callable14 {
        private String val;

        public static void main(String[] args) {
            Callable14 c14 = new Callable14();

            Thread t2 = new Thread(() -> {
                synchronized (c14) {
                    while (c14.val == null) {
                        try {
                            c14.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    System.out.println("return val: " + c14.val);
                }
            });
            Thread t1 = new Thread(() -> {
                synchronized (c14) {
                    c14.val = helloFn.get();
                    t2.interrupt(); // interrupt wait thread
                }
            });

            t1.start();
            t2.start();
        }
    }

    static class Callable15 {

        private String val;

        public static void main(String[] args) {
            Callable15 c15 = new Callable15();

            Thread t2 = new Thread(() -> {
                synchronized (c15) {
                    while (c15.val == null) {
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                            if (c15.val != null) {
                                break;
                            }
                        }
                    }
                    System.out.println("return val: " + c15.val);
                }
            });
            Thread t1 = new Thread(() -> {
                synchronized (c15) {
                    c15.val = helloFn.get();
                    t2.interrupt(); // interrupt wait thread
                }
            });

            t1.start();
            t2.start();
        }
    }

    static class Callable16 {

        private String val;

        public static void main(String[] args) {
            Callable16 c16 = new Callable16();

            Thread t2 = new Thread(() -> {
                synchronized (c16) {
                    while (c16.val == null) {
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    System.out.println("return val: " + c16.val);
                }
            });

            Thread t1 = new Thread(() -> {
                synchronized (c16) {
                    c16.val = helloFn.get();
                }
            });
            t1.setDaemon(true); // daemon
            t1.start();
            t2.start();
        }
    }

    static class Callable17 {

        private String val;

        public static void main(String[] args) {

            SynchronousQueue<Callable17> queue = new SynchronousQueue<>(); // or arrayBlockingQueue

            Thread t1 = new Thread(() -> {
                Callable17 c17 = new Callable17();
                c17.val = helloFn.get();
                try {
                    queue.put(c17);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            });

            Thread t2 = new Thread(() -> {
                try {
                    Callable17 takenObj = queue.take(); // bocking by queue
                    System.out.println("return val: " + takenObj.val);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });

            t1.start();
            t2.start();
        }
    }

    static class Callable18 {

        public static void main(String[] args) {
            String result = CompletableFuture.supplyAsync(helloFn).join(); // compose ...
            System.out.println("return val: " + result);
        }

    }

    static final Supplier<String> helloFn = () -> {
        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return "hello";
    };
}
