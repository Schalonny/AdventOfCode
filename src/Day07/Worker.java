package Day07;

 class Worker {
    private char currentTask;
    private int timeToFinish;

     Worker() {
        this.timeToFinish=-1;
        this.currentTask='-';
    }

     char getCurrentTask() {
        return currentTask;
    }

     void setCurrentTask(char currentTask) {
        this.currentTask = currentTask;
    }

     int getTimeToFinish() {
        return timeToFinish;
    }

     void setTimeToFinish(int timeToFinish) {
        this.timeToFinish = timeToFinish;
    }

     void timesFlows(){
        this.timeToFinish--;
    }
}
