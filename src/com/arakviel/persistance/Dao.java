package com.arakviel.persistance;

import com.arakviel.persistance.exception.DomainJsonIOException;
import com.arakviel.persistance.exception.ModelNotFoundException;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializer;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import java.io.IOException;
import java.io.StringReader;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.Locale;
import java.util.Optional;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import com.arakviel.model.Model;

/**
 * Всі класи, які імплементують інтерфейс, працюють шаром даних та реалізують CRUD функції,
 * пошук/фільтрацію.
 *
 * @param <T> модель бізнес-логіки.
 */
abstract class Dao<T extends Model> {

    //static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    static Gson gson;

    static {
        final DateTimeFormatter serializeFormatter = DateTimeFormatter.ofPattern(
                "dd-MM-yyyy HH:mm");

        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(LocalDateTime.class,
                (JsonSerializer<LocalDateTime>) (localDate, srcType, context) ->
                        new JsonPrimitive(serializeFormatter.format(localDate)));
        gsonBuilder.registerTypeAdapter(LocalDateTime.class,
                (JsonDeserializer<LocalDateTime>) (json, typeOfT, context) ->
                        LocalDateTime.parse(json.getAsString(),
                                DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm")
                                        .withLocale(Locale.of("uk", "UA"))));
        gson = gsonBuilder.setPrettyPrinting().create();
    }

    /**
     * Знайти модель по унікальному ідентифікатору.
     *
     * @param modelId умова рівності по ідентифікатору.
     * @return об'єкт моделі.
     */
    public Optional<T> findById(Predicate<T> modelId) {
        return findAll().stream().filter(modelId).findFirst();
    }

    /**
     * Знайти всі моделі.
     *
     * @return колекцію об'єктів моделі.
     */
    public Set<T> findAll() {
        try {
            fileNotFound();
            var json = Files.readString(this.getPath());
            return isValidJson(json) ? gson.fromJson(json, this.getCollectionType())
                    : new HashSet<>();
        } catch (IOException e) {
            throw new DomainJsonIOException(
                    "Помилка при роботі із файлом %s.".formatted(this.getPath().getFileName()));
        }
    }

    /**
     * Знайти всі моделі за умовою.
     *
     * @param condition умова фільтрування.
     * @return відфільтровану колекцію об'єктів моделі.
     */
    public Set<T> findAll(Predicate<T> condition) {
        Set<T> users = findAll();
        return users.stream().filter(condition).collect(Collectors.toCollection(HashSet::new));
    }

    /**
     * Зберігання моделі у персистентне джерело.
     *
     * @param model об'єкт моделі збереження.
     */
    public void save(T model) {
        Set<T> collection = Files.exists(this.getPath()) ? findAll() : new HashSet<>();
        collection.add(model);
        saveSetToJson(collection);
    }

    /**
     * Збереження множину моделей у персистентне джерело.
     *
     * @param subCollection під колекція моделей.
     */
    public void saveAll(Set<T> subCollection) {
        Set<T> collection = Files.exists(this.getPath()) ? this.findAll() : new HashSet<>();
        collection.addAll(subCollection);
        saveSetToJson(collection);
    }

    /**
     * Оновлення моделі та збереження у персистентне джерело.
     *
     * @param model   об'єкт моделі.
     * @param modelId умова рівності по ідентифікатору.
     */
    public void update(T model, Predicate<T> modelId) {
        Set<T> collection = findAll();

        var fundedModel = findById(modelId).orElseThrow(ModelNotFoundException::new);
        collection.remove(fundedModel);
        collection.add(model);

        saveSetToJson(collection);
    }

    /**
     * Видаленя моделі із колеції та збереження у персистентне джерело по ідентифікатору.
     *
     * @param modelId умова рівності по ідентифікатору.
     */
    public void delete(Predicate<T> modelId) {
        Set<T> collection = findAll();
        var foundedUser = findById(modelId).orElseThrow(ModelNotFoundException::new);
        collection.remove(foundedUser);
        saveSetToJson(collection);
    }

    protected abstract Path getPath();

    /**
     * Колекція моделі, яка серіалізується/десеріалізується в JSON бібліотекою Gson.
     *
     * @return тип колекції.
     */
    protected abstract Type getCollectionType();

    /**
     * Збереження колекції {@link Set} в JSON файл.
     *
     * @param collection коллекція об'єктів моделі.
     */
    private void saveSetToJson(Set<T> collection) {
        if (collection.isEmpty()) {
            return;
        }

        String usersJson = gson.toJson(collection);
        try {
            Files.writeString(this.getPath(), usersJson,
                    StandardOpenOption.CREATE,
                    StandardOpenOption.WRITE,
                    StandardOpenOption.TRUNCATE_EXISTING);
        } catch (IOException e) {
            throw new DomainJsonIOException(
                    "Помилка при збереженні даних в %s.".formatted(this.getPath().getFileName()));
        }
    }

    /**
     * Перевірка на валідність формату даних JSON.
     *
     * @param input JSON у форматі рядка.
     * @return результат перевірки.
     */
    private boolean isValidJson(String input) {
        try (JsonReader reader = new JsonReader(new StringReader(input))) {
            reader.skipValue();
            return reader.peek() == JsonToken.END_DOCUMENT;
        } catch (IOException e) {
            return false;
        }
    }

    /**
     * Якщо файлу не існує, то ми його створюємо.
     *
     * @throws IOException виключення при роботі із потоком вводу виводу.
     */
    private void fileNotFound() throws IOException {
        if (!Files.exists(this.getPath())) {
            Files.createFile(this.getPath());
        }
    }
}