package com.daily.demo.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Header<T> {
    
    private LocalDateTime transactionTime;

    private String resultCode;

    private String description;

    private T data;

    // DATA X OK
    private static <T> Header<T> OK(){
        return (Header<T>) Header.builder()
                    .transactionTime(LocalDateTime.now())
                    .resultCode("OK")
                    .description("OK")
                    .build();
    }

    // DATA O OK
    public static <T> Header<T> OK(T data){                 
        return (Header<T>) Header.builder()
                .transactionTime(LocalDateTime.now())
                .resultCode("OK")
                .description("OK")
                .data(data)
                .build();
    }

    // Error 포함
    public static <T> Header<T> ERROR(String description){      
        return (Header<T>) Header.builder()
                .transactionTime(LocalDateTime.now())
                .resultCode("ERROR")
                .description(description)
                .build();
    }

    //DATA OK(메소드 오버로딩 pagenation)
    // public static <T> Header<T> OK(T data,Pagenation pagenation){                 //매개변수로 data
    //     return (Header<T>) Header.builder()
    //             .transactionTime(LocalDateTime.now())
    //             .resultCode("OK")
    //             .description("OK")
    //             .data(data)
    //             .pagenation(pagenation)
    //             .build();
    // }
}
