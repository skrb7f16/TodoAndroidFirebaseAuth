package com.skrb7f16.todo.Models;

public class Todos {

    String todo,timeStamp,todoId;
    Boolean isDone;

    public Todos(String todo, String timestamp, Boolean isDone) {
        this.todo = todo;
        this.timeStamp = timestamp;
        this.isDone = isDone;
    }


    public Todos(String todo, String timestamp, String todoId, Boolean isDone) {
        this.todo = todo;
        this.timeStamp = timestamp;
        this.todoId = todoId;
        this.isDone = isDone;
    }

    public Todos() {
    }

    public String getTodo() {
        return todo;
    }

    public void setTodo(String todo) {
        this.todo = todo;
    }

    public String getTimestamp() {
        return timeStamp;
    }

    public void setTimestamp(String timestamp) {
        this.timeStamp = timestamp;
    }

    public String getTodoId() {
        return todoId;
    }

    public void setTodoId(String todoId) {
        this.todoId = todoId;
    }

    public Boolean getIsDone() {
        return isDone;
    }

    public void setIsDone(Boolean done) {
        this.isDone = done;
    }
}
