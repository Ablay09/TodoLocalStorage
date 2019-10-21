package kz.mobile.todoapp;

import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

public class ToDo implements Serializable {
    private int id;
    private String name;
    private String description;
    private Date date;

    public ToDo(String name, String description) {
        this.id = UUID.randomUUID().hashCode();
        this.name = name;
        this.description = description;
        this.date = new Date();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ToDo toDo = (ToDo) o;

        if (id != toDo.id) return false;
        if (name != null ? !name.equals(toDo.name) : toDo.name != null) return false;
        if (description != null ? !description.equals(toDo.description) : toDo.description != null)
            return false;
        return date != null ? date.equals(toDo.date) : toDo.date == null;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (description != null ? description.hashCode() : 0);
        result = 31 * result + (date != null ? date.hashCode() : 0);
        return result;
    }
}
