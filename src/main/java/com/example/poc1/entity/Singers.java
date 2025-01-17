package com.example.poc1.entity;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import jakarta.persistence.*;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Objects;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@JacksonXmlRootElement
public class Singers {
    @Id
    @Column(length = 3)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer singerPosition;
    @Column(length = 15)
    private String singerName;
    @Column(length = 15)
    private Double singersRenumeration;

    public void setSingerPosition(int i) {
        this.singerPosition = i;
    }

    public void setSingerName(String name) {
        this.singerName = name;
    }

    public void setSingersRenumeration(double v) {
        this.singersRenumeration = v;
    }

    public Integer getSingerPosition() {
        return singerPosition;
    }
    public String getSingerName() {
        return singerName;
    }
    public Double getSingersRenumeration() {
        return singersRenumeration;
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Singers)) return false;
        Singers singers = (Singers) o;
        return Objects.equals(singerPosition, singers.singerPosition);
    }

    @Override
    public int hashCode() {
        return Objects.hash(singerPosition);
    }
}
