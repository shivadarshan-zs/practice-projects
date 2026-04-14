package com.example.RedisImplimentation.models;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

@Entity
@Table(name = "\"user\"")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class User implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    @Id
    int id;
    String name;
    int age;
}
