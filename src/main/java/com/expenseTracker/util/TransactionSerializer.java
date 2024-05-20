package com.expenseTracker.util;

import com.expenseTracker.entity.Transaction;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.ZoneId;
import java.util.Date;

// Transaction 클래스의 커스텀 직렬화 어댑터
public class TransactionSerializer implements JsonSerializer<Transaction> {
    @Override
    public JsonElement serialize(Transaction transaction, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("id", transaction.getId());
        jsonObject.addProperty("amount", transaction.getAmount());
        jsonObject.addProperty("description", transaction.getDescription());
        jsonObject.addProperty("category", transaction.getCategory().getName());
        jsonObject.addProperty("color", transaction.getCategory().getColor());

        // LocalDateTime -> Instant -> Date -> moment.js 형태로 변환
        Instant instant = transaction.getDate().atZone(ZoneId.systemDefault()).toInstant();
        Date date = Date.from(instant);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        jsonObject.addProperty("date", dateFormat.format(date));

        return jsonObject;
    }
}
