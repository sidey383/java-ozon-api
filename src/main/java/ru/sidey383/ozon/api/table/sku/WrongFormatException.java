package ru.sidey383.ozon.api.table.sku;

import java.io.IOException;

public class WrongFormatException extends IOException {

    public WrongFormatException(String reason) {
        super(reason);
    }

}
