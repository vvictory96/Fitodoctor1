package com.example.asus;



//Вспомогательный класс болезни. Для хранения в списке и удобства передачи данных в RecyclerView
public class Disease {
    int idD; //Идентификатор болезни
    int idPict; //идентификатор картинки
    String NameD; //Название болезни
    String SymptD; //Описание симптомов
    String DesrD; //Описание болезни
    String TreatD; //Описание лечения

    Disease(int i, int p, String Nm, String Sm, String Dd, String Td)
    {
        idD=i;
        idPict=p;
        NameD=Nm;
        SymptD=Sm;
        DesrD=Dd;
        TreatD=Td;
    }

}
