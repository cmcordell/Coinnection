package personal.calebcordell.coinnection.domain.model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import java.util.UUID;

@Entity(tableName = "portfolios")
public class Portfolio {

    @PrimaryKey
    @NonNull
    private String id; //Unique id for portfolio
    private String name; //Name of portfolio ie My Portfolio

    public Portfolio() {
        this.id = UUID.randomUUID().toString();
    }

    @NonNull
    public String getId() {
        return id;
    }

    public void setId(@NonNull String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String newName) {
        name = newName;
    }
}