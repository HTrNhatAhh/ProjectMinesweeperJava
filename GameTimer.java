public class GameTimer {
    private void restartThread() {
        Thread th = new Thread(new Runnable() {
            public void run() {
                while (state == GameState.Playing) {
                    lblTimer.setText(Long.parseLong(lblTimer.getText()) + 1 + "");
                    lblTimer.updateUI();
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        th.start();
    }

}
