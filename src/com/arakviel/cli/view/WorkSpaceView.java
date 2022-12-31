package com.arakviel.cli.view;

import static java.lang.System.out;
import static com.arakviel.cli.util.ScannerObjectHolder.SCANNER;

import at.favre.lib.crypto.bcrypt.BCrypt;
import com.arakviel.cli.item.MainMenuItem;
import com.arakviel.cli.item.WorkSpaceMenuItem;
import com.arakviel.cli.partial.SeparatorPartial;
import com.arakviel.cli.partial.TryAgainPartial;
import com.arakviel.cli.partial.UserChoicePartial;
import com.arakviel.cli.partial.page.WorkSpacePartial;
import com.arakviel.model.impl.Client;
import com.arakviel.model.impl.Money;
import com.arakviel.model.impl.Seat;
import com.arakviel.model.impl.Station;
import com.arakviel.model.impl.Ticket;
import com.arakviel.model.impl.Train;
import com.arakviel.model.impl.User;
import com.arakviel.repository.ClientRepository;
import com.arakviel.repository.StationRepository;
import com.arakviel.repository.TicketRepository;
import com.arakviel.repository.TrainRepository;
import com.arakviel.repository.UserRepository;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Queue;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import com.arakviel.cli.util.AnsiColor;
import com.arakviel.cli.util.TableFormatter;
import com.arakviel.cli.util.YesNoInput;

public final class WorkSpaceView {

    private static final int INPUT_SIZE = WorkSpaceMenuItem.values().length;
    private static MainMenuItem selectedItem;

    public static void init(MainMenuItem item) {
        selectedItem = item;
        WorkSpacePartial.init(item);
        UserChoicePartial.initWithReturn(
                INPUT_SIZE,
                id -> Arrays.stream(WorkSpaceMenuItem.values())
                        .filter(i -> i.ordinal() == id)
                        .findFirst()
                        .orElseThrow()
                        .run(),
                MainView::init
        );
    }

    public static class Process {

        public static void run(WorkSpaceMenuItem item) {
            SeparatorPartial.init();
            switch (selectedItem) {
                case TICKET -> TicketProcess.run(item);
                case CLIENT -> ClientProcess.run(item);
                case STATION -> StationProcess.run(item);
                case TRAIN -> TrainProcess.run(item);
                case USER -> UserProcess.run(item);
            }
        }

        private Process() {
        }
    }

    private static class ClientProcess {

        private static final String CAPTION = "пасажира";

        public static void run(WorkSpaceMenuItem item) {

            switch (item) {
                case ADD -> {
                    Client client = buildCLient(UUID.randomUUID());
                    ClientRepository.add(client);
                    initSuccess("додавання");
                }
                case VIEW -> printAsTable();
                case EDIT -> {
                    printAsTable();
                    UUID selectedId = getSelectedClientId("редагування");
                    Client client = buildCLient(selectedId);
                    ClientRepository.set(selectedId, client);
                    initSuccess("редагування");
                }
                case DELETE -> {
                    printAsTable();
                    UUID selectedId = getSelectedClientId("видалення");
                    out.printf("%sВи впевнені, що хочете видалити обраного %s? (Y/N): ",
                            AnsiColor.RED_BOLD, CAPTION);
                    String input = SCANNER.next().toLowerCase();
                    if (input.equals(YesNoInput.YES.toString())) {
                        ClientRepository.remove(selectedId);
                        initSuccess("видалення");
                    }
                }
            }

            // Повертаємося до вибраного робочого пространства
            WorkSpaceView.init(selectedItem);
        }

        private static Client buildCLient(UUID id) {
            out.printf("%sІм'я: ", AnsiColor.RESET);
            String name = SCANNER.next();
            out.printf("%sПризвіще: ", AnsiColor.RESET);
            String surname = SCANNER.next();
            Client client = Client.builder()
                    .id(id)
                    .name(name)
                    .surname(surname)
                    .build();

            if (canBuildClient()) {
                return client;
            } else {
                final Client[] clientFromRecursion = new Client[1];
                TryAgainPartial.init(() -> clientFromRecursion[0] = buildCLient(id),
                        () -> WorkSpaceView.init(selectedItem));
                return clientFromRecursion[0];
            }
        }

        private static boolean canBuildClient() {
            Queue<String> errors = Client.Validation.getErrors();
            boolean emptyErrors = errors.isEmpty();
            if (!emptyErrors) {
                SeparatorPartial.init();

                out.printf("%sЄ помилки при заповненні %s. Слідуйте підсказкам:%n", AnsiColor.RED,
                        CAPTION);
                errors.forEach(e -> out.printf("- %s%n", e));
            }
            return emptyErrors;
        }

        private static void initSuccess(String caption) {
            out.printf("%sОперація %s в \"%s\" успішно виконана.%s%n", AnsiColor.GREEN_UNDERLINED,
                    caption, selectedItem.getName(),
                    AnsiColor.RESET);
        }

        private static void printAsTable() {
            var rows = ClientRepository.getAll().stream()
                    .map(c -> List.of(c.getId().toString(), c.getName(), c.getSurname()))
                    .collect(Collectors.toCollection(LinkedList::new));
            String table = TableFormatter.run(List.of("ID", "Ім'я", "Прізвище"), rows);
            out.print(table);
        }

        private static UUID getSelectedClientId(String caption) {
            out.printf("%sОберіть номер рядка для %s%n%s".formatted(
                    AnsiColor.YELLOW, caption, AnsiColor.RESET));
            out.printf("%s>>> ", selectedItem.getColor());
            int selectedNumber = SCANNER.nextInt() - 1;
            List<Client> clients = ClientRepository.getAll().stream().toList();
            Client selectedClient = clients.get(selectedNumber);
            UUID selectedId = selectedClient.getId();
            out.printf("%sОбраний клієнт: %s".formatted(AnsiColor.PURPLE, AnsiColor.RESET));
            out.println(selectedClient);
            return selectedId;
        }

        private ClientProcess() {
        }
    }

    private static class StationProcess {

        private static final String CAPTION = "станція";

        public static void run(WorkSpaceMenuItem item) {

            switch (item) {
                case ADD -> {
                    Station station = buildStation();
                    StationRepository.add(station);
                    initSuccess("додавання");
                }
                case VIEW -> printAsTable();
                case EDIT -> {
                    printAsTable();
                    int selectedId = getSelectedStationCode("редагування");
                    Station station = buildStation(selectedId);
                    StationRepository.set(selectedId, station);
                    initSuccess("редагування");
                }
                case DELETE -> {
                    printAsTable();
                    int selectedId = getSelectedStationCode("видалення");
                    out.printf("%sВи впевнені, що хочете видалити обрану станцію? (Y/N): ",
                            AnsiColor.RED_BOLD);
                    String input = SCANNER.next().toLowerCase();
                    if (input.equals(YesNoInput.YES.toString())) {
                        StationRepository.remove(selectedId);
                        initSuccess("видалення");
                    }
                }
            }

            // Повертаємося до вибраного робочого пространства
            WorkSpaceView.init(selectedItem);
        }

        private static Station buildStation() {
            return buildStation(0);
        }

        private static Station buildStation(final int oldUnm) {
            out.printf("%sКод ЄМР: ", AnsiColor.RESET);
            int unm;
            if (oldUnm == 0) {
                unm = SCANNER.nextInt();
            } else {
                unm = oldUnm;
                out.println(unm);
            }
            out.printf("%sНазва станції: ", AnsiColor.RESET);
            String name = SCANNER.next();
            Station station = Station.builder()
                    .unm(unm)
                    .name(name)
                    .build();

            boolean isAddOperation = unm == oldUnm || isStationExist(unm);

            if (canBuildStation() && isAddOperation) {
                return station;
            } else {
                final Station[] stationFromRecursion = new Station[1];
                TryAgainPartial.init(() -> stationFromRecursion[0] = buildStation(oldUnm),
                        () -> WorkSpaceView.init(selectedItem));
                return stationFromRecursion[0];
            }
        }

        private static boolean canBuildStation() {
            Queue<String> errors = Station.Validation.getErrors();
            boolean emptyErrors = errors.isEmpty();
            if (!emptyErrors) {
                SeparatorPartial.init();

                out.printf("%sЄ помилки при заповненні %s. Слідуйте підсказкам:%n", AnsiColor.RED,
                        CAPTION);
                errors.forEach(e -> out.printf("- %s%n", e));
            }
            return emptyErrors;
        }

        private static boolean isStationExist(int unm) {
            boolean isValid = true;
            if (Objects.nonNull(StationRepository.get(unm).orElse(null))) {
                out.printf("%sСтанція під таким ЄМР вже існує.%n", AnsiColor.RED);
                isValid = false;
            }
            return isValid;
        }

        private static void initSuccess(String caption) {
            out.printf("%sОперація %s в \"%s\" успішно виконана.%s%n", AnsiColor.GREEN_UNDERLINED,
                    caption, selectedItem.getName(),
                    AnsiColor.RESET);
        }

        private static void printAsTable() {
            var rows = StationRepository.getAll().stream()
                    .map(s -> List.of(Integer.toString(s.getUnm()), s.getName()))
                    .collect(Collectors.toCollection(LinkedList::new));
            String table = TableFormatter.run(List.of("ЄМР", "Назва станції"), rows);
            out.print(table);
        }

        private static int getSelectedStationCode(String caption) {
            out.printf("%sОберіть номер рядка для %s%n%s".formatted(
                    AnsiColor.YELLOW, caption, AnsiColor.RESET));
            out.printf("%s>>> ", selectedItem.getColor());
            int selectedNumber = SCANNER.nextInt() - 1;
            List<Station> stations = StationRepository.getAll().stream().toList();
            Station selectedStation = stations.get(selectedNumber);
            int selectedId = selectedStation.getUnm();
            out.printf("%sОбрана станція: %s".formatted(AnsiColor.PURPLE, AnsiColor.RESET));
            out.println(selectedStation);
            return selectedId;
        }

        private StationProcess() {
        }
    }

    private static class TrainProcess {

        private static final String CAPTION = "потяг";

        public static void run(WorkSpaceMenuItem item) {

            switch (item) {
                case ADD -> {
                    Train train = buildTrain();
                    TrainRepository.add(train);
                    initSuccess("додавання");
                }
                case VIEW -> printAsTable();
                case EDIT -> {
                    printAsTable();
                    int selectedId = getSelectedTrainCode("редагування");
                    Train train = buildTrain(selectedId);
                    TrainRepository.set(selectedId, train);
                    initSuccess("редагування");
                }
                case DELETE -> {
                    printAsTable();
                    int selectedId = getSelectedTrainCode("видалення");
                    out.printf("%sВи впевнені, що хочете видалити обрану станцію? (Y/N): ",
                            AnsiColor.RED_BOLD);
                    String input = SCANNER.next().toLowerCase();
                    if (input.equals(YesNoInput.YES.toString())) {
                        TrainRepository.remove(selectedId);
                        initSuccess("видалення");
                    }
                }
            }

            // Повертаємося до вибраного робочого пространства
            WorkSpaceView.init(selectedItem);
        }

        private static Train buildTrain() {
            return buildTrain(0);
        }

        private static Train buildTrain(final int oldCode) {
            out.printf("%sКод потяга: ", AnsiColor.RESET);
            int code;
            if (oldCode == 0) {
                code = SCANNER.nextInt();
            } else {
                code = oldCode;
                out.println(code);
            }
            out.printf("%sДоповнення %s(впишіть символ \"_\", щоб пропустити)%s: ", AnsiColor.RESET,
                    AnsiColor.YELLOW,
                    AnsiColor.RESET);
            String addon = SCANNER.next();
            addon = addon.equals("_") ? "" : addon;
            Train train = new Train(code);
            train.setAddon(addon);

            boolean isAddOperation = code == oldCode || isTrainExist(code);

            if (canBuildTrain() && isAddOperation) {
                return train;
            } else {
                final Train[] trainFromRecursion = new Train[1];
                TryAgainPartial.init(() -> trainFromRecursion[0] = buildTrain(oldCode),
                        () -> WorkSpaceView.init(selectedItem));
                return trainFromRecursion[0];
            }
        }

        private static boolean canBuildTrain() {
            Queue<String> errors = Train.Validation.getErrors();
            boolean emptyErrors = errors.isEmpty();
            if (!emptyErrors) {
                SeparatorPartial.init();
                out.printf("%sЄ помилки при заповненні %s. Слідуйте підсказкам:%n", AnsiColor.RED,
                        CAPTION);
                errors.forEach(e -> out.printf("- %s%n", e));
            }
            return emptyErrors;
        }

        private static boolean isTrainExist(int unm) {
            boolean isValid = true;
            if (Objects.nonNull(TrainRepository.get(unm).orElse(null))) {
                out.printf("%sПотяга під таким кодом вже існує.%n", AnsiColor.RED);
                isValid = false;
            }
            return isValid;
        }

        private static void initSuccess(String caption) {
            out.printf("%sОперація %s в \"%s\" успішно виконана.%s%n", AnsiColor.GREEN_UNDERLINED,
                    caption, selectedItem.getName(),
                    AnsiColor.RESET);
        }

        private static void printAsTable() {
            var rows = TrainRepository.getAll().stream()
                    .map(s -> List.of(Integer.toString(s.getCode()), s.getAddon()))
                    .collect(Collectors.toCollection(LinkedList::new));
            String table = TableFormatter.run(List.of("Код потяга", "Назва станції"), rows);
            out.print(table);
        }

        private static int getSelectedTrainCode(String caption) {
            out.printf("%sОберіть номер рядка для %s%n%s".formatted(
                    AnsiColor.YELLOW, caption, AnsiColor.RESET));
            out.printf("%s>>> ", selectedItem.getColor());
            int selectedNumber = SCANNER.nextInt() - 1;
            List<Train> trains = TrainRepository.getAll().stream().toList();
            Train selectedTrain = trains.get(selectedNumber);
            int selectedCode = selectedTrain.getCode();
            out.printf("%sОбраний потяг: %s".formatted(AnsiColor.PURPLE, AnsiColor.RESET));
            out.println(selectedTrain);
            return selectedCode;
        }

        private TrainProcess() {
        }
    }

    private static class UserProcess {

        private static final String CAPTION = "робітника";

        public static void run(WorkSpaceMenuItem item) {

            switch (item) {
                case ADD -> {
                    User user = buildUser(UUID.randomUUID());
                    user.setPassword(BCrypt.withDefaults()
                            .hashToString(12, user.getPassword().toCharArray()));
                    UserRepository.add(user);
                    initSuccess("додавання");
                }
                case VIEW -> printAsTable();
                case EDIT -> {
                    printAsTable();
                    UUID selectedId = getSelectedUserId("редагування");
                    User user = buildUser(selectedId);
                    user.setPassword(BCrypt.withDefaults()
                            .hashToString(12, user.getPassword().toCharArray()));
                    UserRepository.set(selectedId, user);
                    initSuccess("редагування");
                }
                case DELETE -> {
                    printAsTable();
                    UUID selectedId = getSelectedUserId("видалення");
                    out.printf("%sВи впевнені, що хочете видалити обраного %s? (Y/N): ",
                            AnsiColor.RED_BOLD, CAPTION);
                    String input = SCANNER.next().toLowerCase();
                    if (input.equals(YesNoInput.YES.toString())) {
                        UserRepository.remove(selectedId);
                        initSuccess("видалення");
                    }
                }
            }

            // Повертаємося до вибраного робочого пространства
            WorkSpaceView.init(selectedItem);
        }

        private static User buildUser(UUID id) {
            out.printf("%sЛогін: ", AnsiColor.RESET);
            String login = SCANNER.next();
            out.printf("%sПароль: ", AnsiColor.RESET);
            String password = SCANNER.next();

            out.printf("%sВиберіть роль користувача: %n", AnsiColor.RESET);
            Arrays.stream(User.Role.values())
                    .map(r -> "[%d] %s".formatted(r.ordinal() + 1, r.name().toLowerCase()))
                    .forEach(out::println);

            out.printf("%s>>> ", selectedItem.getColor());
            int roleId = SCANNER.nextInt() - 1;
            // валідація діапазону допустимих значень
            if (roleId >= User.Role.values().length || roleId < 0) {
                out.printf("%sНе вірне значення. Допустимий діапазон від 1 до %d %s%n",
                        AnsiColor.RED,
                        User.Role.values().length, AnsiColor.RESET);
                TryAgainPartial.init(() -> buildUser(id),
                        () -> WorkSpaceView.init(selectedItem));
            }
            //==
            User.Role role = User.Role.values()[roleId];

            User user = User.builder()
                    .id(id)
                    .login(login)
                    .password(password)
                    .role(role)
                    .build();

            if (canBuildUser()) {
                return user;
            } else {
                final User[] userFromRecursion = new User[1];
                TryAgainPartial.init(() -> userFromRecursion[0] = buildUser(id),
                        () -> WorkSpaceView.init(selectedItem));
                return userFromRecursion[0];
            }
        }

        private static boolean canBuildUser() {
            Queue<String> errors = User.Validation.getErrors();
            boolean emptyErrors = errors.isEmpty();
            if (!emptyErrors) {
                SeparatorPartial.init();

                out.printf("%sЄ помилки при заповненні %s. Слідуйте підсказкам:%n", AnsiColor.RED,
                        CAPTION);
                errors.forEach(e -> out.printf("- %s%n", e));
            }
            return emptyErrors;
        }

        private static void initSuccess(String caption) {
            out.printf("%sОперація %s в \"%s\" успішно виконана.%s%n", AnsiColor.GREEN_UNDERLINED,
                    caption, selectedItem.getName(),
                    AnsiColor.RESET);
        }

        private static void printAsTable() {
            var rows = UserRepository.getAll().stream()
                    .map(u -> List.of(u.getId().toString(), u.getLogin(), u.getPassword(),
                            u.getRole().name().toLowerCase()))
                    .collect(Collectors.toCollection(LinkedList::new));
            String table = TableFormatter.run(List.of("ID", "Логін", "Пароль", "Роль"), rows);
            out.print(table);
        }

        private static UUID getSelectedUserId(String caption) {
            out.printf("%sОберіть номер рядка для %s%n%s".formatted(
                    AnsiColor.YELLOW, caption, AnsiColor.RESET));
            out.printf("%s>>> ", selectedItem.getColor());
            int selectedNumber = SCANNER.nextInt() - 1;
            List<User> users = UserRepository.getAll().stream().toList();

            // валідація діапазону допустимих значень
            if (selectedNumber >= users.size() || selectedNumber < 0) {
                out.printf("%sНе вірне значення. Допустимий діапазон від 1 до %d %s%n",
                        AnsiColor.RED,
                        users.size(), AnsiColor.RESET);
                TryAgainPartial.init(() -> getSelectedUserId(caption),
                        () -> WorkSpaceView.init(selectedItem));
            }
            //==

            User selectedUser = users.get(selectedNumber);
            UUID selectedId = selectedUser.getId();
            out.printf("%sОбраний працівник: %s".formatted(AnsiColor.PURPLE, AnsiColor.RESET));
            out.println(selectedUser);
            return selectedId;
        }

        private UserProcess() {
        }
    }

    private static class TicketProcess {

        private static final String CAPTION = "робітника";

        public static void run(WorkSpaceMenuItem item) {

            switch (item) {
                case ADD -> {
                    Ticket ticket = buildTicket(UUID.randomUUID());
                    TicketRepository.add(ticket);
                    initSuccess("додавання");
                }
                case VIEW -> printAsTable();
                case EDIT -> {
                    printAsTable();
                    UUID selectedId = getSelectedTicketId("редагування");
                    Ticket ticket = buildTicket(selectedId);
                    TicketRepository.set(selectedId, ticket);
                    initSuccess("редагування");
                }
                case DELETE -> {
                    printAsTable();
                    UUID selectedId = getSelectedTicketId("видалення");
                    out.printf("%sВи впевнені, що хочете видалити обраного %s? (Y/N): ",
                            AnsiColor.RED_BOLD, CAPTION);
                    String input = SCANNER.next().toLowerCase();
                    if (input.equals(YesNoInput.YES.toString())) {
                        TicketRepository.remove(selectedId);
                        initSuccess("видалення");
                    }
                }
            }

            // Повертаємося до вибраного робочого пространства
            WorkSpaceView.init(selectedItem);
        }

        private static Ticket buildTicket(UUID id) {
            // CLIENT
            out.printf("%sВиберіть клієнта: %s%n", AnsiColor.CYAN, AnsiColor.RESET);
            Set<Client> clients = ClientRepository.getAll();
            var clientRows = clients.stream()
                    .map(c -> List.of(c.getId().toString(), c.getName(), c.getSurname()))
                    .collect(Collectors.toCollection(LinkedList::new));
            String clientTable = TableFormatter.run(List.of("ID", "Ім'я", "Прізвище"), clientRows);
            out.print(clientTable);

            out.printf("%s>>> ", selectedItem.getColor());
            int clientId = SCANNER.nextInt() - 1;
            // валідація діапазону допустимих значень
            if (clientId >= clients.size() || clientId < 0) {
                out.printf("%sНе вірне значення. Допустимий діапазон від 1 до %d %s%n",
                        AnsiColor.RED, clients.size(), AnsiColor.RESET);
                TryAgainPartial.init(() -> buildTicket(id),
                        () -> WorkSpaceView.init(selectedItem));
            }
            //==
            List<Client> clientList = clients.stream().toList();
            Client client = clientList.get(clientId);

            //TRAIN
            out.printf("%sВиберіть поїзд: %s%n", AnsiColor.CYAN, AnsiColor.RESET);
            Set<Train> trains = TrainRepository.getAll();
            var trainRows = trains.stream()
                    .map(t -> List.of(Integer.toString(t.getCode()), t.getAddon()))
                    .collect(Collectors.toCollection(LinkedList::new));
            String trainTable = TableFormatter.run(List.of("Код", "Примітка"), trainRows);
            out.print(trainTable);

            out.printf("%s>>> ", selectedItem.getColor());
            int trainId = SCANNER.nextInt() - 1;
            // валідація діапазону допустимих значень
            if (trainId >= trains.size() || trainId < 0) {
                out.printf("%sНе вірне значення. Допустимий діапазон від 1 до %d %s%n",
                        AnsiColor.RED, trains.size(), AnsiColor.RESET);
                TryAgainPartial.init(() -> buildTicket(id),
                        () -> WorkSpaceView.init(selectedItem));
            }
            //==
            List<Train> trainList = trains.stream().toList();
            Train train = trainList.get(trainId);

            // Train Carriage
            out.printf("%sВагон поїзду (формату 10 купе): %s", AnsiColor.CYAN, AnsiColor.RESET);
            SCANNER.nextLine();// без цього, не працює.
            String trainCarriage = SCANNER.nextLine();

            // STATION
            out.printf("%sВиберіть станцію звідки та куди відправка: %s%n", AnsiColor.CYAN,
                    AnsiColor.RESET);
            Set<Station> stations = StationRepository.getAll();
            var stationRows = stations.stream()
                    .map(t -> List.of(Integer.toString(t.getUnm()), t.getName()))
                    .collect(Collectors.toCollection(LinkedList::new));
            String stationTable = TableFormatter.run(List.of("ЄМР", "Назва"), stationRows);
            out.print(stationTable);

            out.printf("%s>>> (звідки) ", selectedItem.getColor());
            int stationIdFrom = SCANNER.nextInt() - 1;
            // валідація діапазону допустимих значень
            if (stationIdFrom >= stations.size() || stationIdFrom < 0) {
                out.printf("%sНе вірне значення. Допустимий діапазон від 1 до %d %s%n",
                        AnsiColor.RED, stations.size(), AnsiColor.RESET);
                TryAgainPartial.init(() -> buildTicket(id),
                        () -> WorkSpaceView.init(selectedItem));
            }
            out.printf("%s>>> (куди) ", selectedItem.getColor());
            int stationIdTo = SCANNER.nextInt() - 1;
            if (stationIdTo >= stations.size() || stationIdTo < 0) {
                out.printf("%sНе вірне значення. Допустимий діапазон від 1 до %d %s%n",
                        AnsiColor.RED, stations.size(), AnsiColor.RESET);
                TryAgainPartial.init(() -> buildTicket(id),
                        () -> WorkSpaceView.init(selectedItem));
            }
            //==
            List<Station> stationList = stations.stream().toList();
            Station stationFrom = stationList.get(stationIdFrom);
            Station stationTo = stationList.get(stationIdTo);

            // departure
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");

            out.printf("%sДата та час відправки (формату 01-01-2022 12:00): %s", AnsiColor.CYAN,
                    AnsiColor.RESET);
            SCANNER.nextLine();// без цього, не працює.
            String departureString = SCANNER.nextLine();
            LocalDateTime departure = LocalDateTime.parse(departureString, formatter);

            // arrival
            out.printf("%sДата та час приїзду (формату 01-01-2022 12:00): %s", AnsiColor.CYAN,
                    AnsiColor.RESET);
            String arrivalString = SCANNER.nextLine();
            LocalDateTime arrival = LocalDateTime.parse(arrivalString, formatter);

            // Money
            out.printf("%sЦіна (формату 100.50 (100 грн 50 копійок)): %s", AnsiColor.CYAN,
                    AnsiColor.RESET);
            String money = SCANNER.next();
            String[] moneySplit = money.split("\\.");
            int moneyGrn = Integer.parseInt(moneySplit[0]);
            int moneyCoin = Integer.parseInt(moneySplit[1]);

            // Seat
            out.printf("%sНомер сидіння: %s", AnsiColor.CYAN, AnsiColor.RESET);
            int number = SCANNER.nextInt();
            out.printf("%sВиберіть тип квитка: %s%n", AnsiColor.CYAN, AnsiColor.RESET);
            Arrays.stream(Seat.Type.values())
                    .map(t -> "[%d] %s".formatted(t.ordinal() + 1, t.getName()))
                    .forEach(out::println);

            out.printf("%s>>> ", selectedItem.getColor());
            int typeId = SCANNER.nextInt() - 1;
            // валідація діапазону допустимих значень
            if (typeId > Seat.Type.values().length || typeId < 0) {
                TryAgainPartial.init(() -> buildTicket(id),
                        () -> WorkSpaceView.init(selectedItem));
            }
            //==
            Seat.Type type = Seat.Type.values()[typeId];

            Seat seat = Seat.builder()
                    .number(number)
                    .type(type)
                    .build();

            // TICKET
            Ticket ticket = Ticket.builder()
                    .id(id)
                    .client(client)
                    .train(train)
                    .trainCarriage(trainCarriage)
                    .from(stationFrom)
                    .to(stationTo)
                    .departure(departure)
                    .arrival(arrival)
                    .money(new Money(moneyGrn, moneyCoin))
                    .seat(seat)
                    .build();

            if (canBuildTicket()) {
                return ticket;
            } else {
                final Ticket[] ticketFromRecursion = new Ticket[1];
                TryAgainPartial.init(() -> ticketFromRecursion[0] = buildTicket(id),
                        () -> WorkSpaceView.init(selectedItem));
                return ticketFromRecursion[0];
            }
        }

        private static boolean canBuildTicket() {
            Queue<String> errors = Seat.Validation.getErrors();
            //Queue<String> errors = Ticket.Validation.getErrors();
            boolean emptyErrors = errors.isEmpty();
            if (!emptyErrors) {
                SeparatorPartial.init();

                out.printf("%sЄ помилки при заповненні %s. Слідуйте підсказкам:%n", AnsiColor.RED,
                        CAPTION);
                errors.forEach(e -> out.printf("- %s%n", e));
            }
            return emptyErrors;
        }

        private static void initSuccess(String caption) {
            out.printf("%sОперація %s в \"%s\" успішно виконана.%s%n", AnsiColor.GREEN_UNDERLINED,
                    caption, selectedItem.getName(),
                    AnsiColor.RESET);
        }

        private static void printAsTable() {
            final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm (dd MMM yyyy)",
                    Locale.of("uk", "UA"));

            var rows = TicketRepository.getAll().stream()
                    .map(t -> List.of(t.getId().toString(), t.getClient().toString(),
                            t.getTrain().toString(), t.getTrainCarriage(),
                            t.getFrom().toString(), t.getTo().toString(),
                            t.getDeparture().format(formatter),
                            t.getArrival().format(formatter),
                            t.getMoney().toString(), t.getSeat().toString()))
                    .collect(Collectors.toCollection(LinkedList::new));
            String table = TableFormatter.run(
                    List.of("ID", "Пасажир", "Потяг", "Вагон потягу", "Звідки", "Куди", "Відправка",
                            "Прибуття", "Ціна", "Місце"), rows);
            out.print(table);
        }

        private static UUID getSelectedTicketId(String caption) {
            out.printf("%sОберіть номер рядка для %s%n%s".formatted(
                    AnsiColor.YELLOW, caption, AnsiColor.RESET));
            out.printf("%s>>> ", selectedItem.getColor());
            int selectedNumber = SCANNER.nextInt() - 1;
            List<Ticket> tickets = TicketRepository.getAll().stream().toList();

            // валідація діапазону допустимих значень
            if (selectedNumber >= tickets.size() || selectedNumber < 0) {
                out.printf("%sНе вірне значення. Допустимий діапазон від 1 до %d %s%n",
                        AnsiColor.RED,
                        tickets.size(), AnsiColor.RESET);
                TryAgainPartial.init(() -> getSelectedTicketId(caption),
                        () -> WorkSpaceView.init(selectedItem));
            }
            //==

            Ticket selectedTicket = tickets.get(selectedNumber);
            UUID selectedId = selectedTicket.getId();
            out.printf("%sОбраний квиток: %s".formatted(AnsiColor.PURPLE, AnsiColor.RESET));
            out.println(selectedTicket);
            return selectedId;
        }

        private TicketProcess() {
        }
    }

    private WorkSpaceView() {
    }
}
