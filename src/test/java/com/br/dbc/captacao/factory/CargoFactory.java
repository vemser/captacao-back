package com.br.dbc.captacao.factory;

import com.br.dbc.captacao.dto.CargoDTO;
import com.br.dbc.captacao.entity.CargoEntity;

import java.util.Collections;

public class CargoFactory {
    public static CargoEntity getCargoEntity() {
        return new CargoEntity(2,
                "ADMIN"
        );
    }

    public static CargoDTO getCargoDTO() {
        return new CargoDTO(1,"ADMIN");
    }
}
