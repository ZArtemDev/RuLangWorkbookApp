package com.ZArtemDev.RuLangWorkbookApp.student.task;

import javafx.scene.Node;
import java.util.LinkedList;

public abstract class Task {
    public abstract LinkedList<Node> getContentList();
    public abstract String getAnswer(LinkedList<Node> ll);
    public abstract LinkedList coverIntoContainers(LinkedList<Node> ll);
}
