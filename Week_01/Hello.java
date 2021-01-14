public class Hello {

    public int add(int x, int y) {
        return x + y;
    }

    public int subtract(int x, int y) {
        return x - y;
    }

    public int multiply(int x, int y) {
        return x * y;
    }

    public int divide(int x, int y) {
        return x / y;
    }

    public int mod(int x, int y) {
        return x % y;
    }

    public void nonUsedLocalVar() {
        int a; // no byte instruction
        int b = 15;
    }

    public void callAdd() {
        this.add(1, 2); // invokevirtual: instance method
    }

    protected static int staticAdd(int x, int y) {
        return x + y;
    }

    public static void callStaticAdd() {
        staticAdd(1, 2); // invokestatic: instance method
    }

    // for loop and if/else
    public void loop() {
        int[] arr = new int[10];
        for (int i = 0; i < 10; i++) {
            if (i % 2 == 0) {
                arr[i] = i * 2;
            } else {
                arr[i] = i;
            }
        }
    }

    // exception
    public void checkRange(int i) throws Exception {
        if (i < 0) {
            throw new Exception("number should not be negative");
        }
    }

    public boolean isAlphaNum(String str) {
        boolean flag = true;
        for (char ch : str.toCharArray()) {
            if (!((ch > 'A' && ch <= 'Z') || (ch > 'a' && ch <= 'z') || (ch > '0' && ch <= '9'))) {
                flag = false;
            }
        }
        return flag;
    }

    interface Greeting {
        void hi();
    }

    class HelloChild extends Hello implements Greeting {

        @Override
        public void hi() {

        }

        @Override
        public int add(int x, int y) {
            return super.add(x, y); // invokeSpecial: super call
        }


        @Override
        public int subtract(int x, int y) {
            return x - y;
        }

        public void methodDispatchDemo() {
            Hello h = new Hello(); // invokeSpecial: init
            h.add(1, 2); // invokevirtual: instance

            Greeting g = new HelloChild();
            g.hi(); // invokeInterface
            ((HelloChild) g).add(1, 2); // checkcast; invokevirtual: helloChild.add

            Hello h2 = new HelloChild();
            h2.add(1, 2);
            h2.subtract(1, 2);

            HelloChild hc1 = new HelloChild();
            hc1.hi(); // invokevirtual
            hc1.add(1, 2);
            hc1.subtract(1, 2);
        }
    }
}
