package com.mehmetyilmazalan.miniecommerce.repository;

import java.util.List;

public interface Repository <T,ID>{

    void ekle(T nesne);

    T getir(ID id);

    void guncelle(T nesne);

    List<T> listele();

    boolean varMi(ID id);

}
