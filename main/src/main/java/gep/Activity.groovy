package gep

class Activity {
    int contentView;

    protected void onCreate(Object savedInstanceState) {
        println(savedInstanceState)
    }

    void setContentView(int contentView) {
        println("seting content view $contentView")
        this.contentView = contentView
    }
}
