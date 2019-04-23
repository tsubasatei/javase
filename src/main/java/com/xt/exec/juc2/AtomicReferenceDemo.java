package com.xt.exec.juc2;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import java.util.concurrent.atomic.AtomicReference;

/**
 * 原子引用
 */
@Getter
@AllArgsConstructor
@ToString
class User{
    private String name;
    private int age;
}
public class AtomicReferenceDemo {

    public static void main(String[] args) {
        User user1 = new User("zhangsan", 28);
        User user2 = new User("lisi", 30);

        AtomicReference<User> atomicReference = new AtomicReference<>();
        atomicReference.set(user1);

        System.out.println(atomicReference.compareAndSet(user1, user2) + "\t" + atomicReference.get().toString());
        System.out.println(atomicReference.compareAndSet(user1, user2) + "\t" + atomicReference.get().toString());
    }
}
