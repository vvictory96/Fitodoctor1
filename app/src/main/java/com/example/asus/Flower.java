package com.example.asus;


//Вспомогательный класс растения. Для хранения в списке и удобства передачи данных в RecyclerView
public class Flower {
     int ID; //Идентификатор растения
     String NameFl; //Название растения
     int KodImg; //Идентификатор картинки

     public Flower(int d, String n, int p)
     {
         ID=d;
         NameFl=n;
         KodImg=p;
     }

}
