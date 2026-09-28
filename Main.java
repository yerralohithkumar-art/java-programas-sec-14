import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;
import java.io.*;
import java.net.InetSocketAddress;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.*;

public class Main {

    // ==========================================
    // BANK SETTINGS
    // ==========================================

    static final int PORT = 8080;
    static final String FILE_NAME = "accounts.txt";

    static final String ADMIN_NAME = "lohith";
    static final String ADMIN_PASSWORD = "lohithbank";
    static final long ADMIN_ACCOUNT = 9999999999L;

    static final int MAX_USERS = 1000;

    static String[] names = new String[MAX_USERS];
    static String[] passwords = new String[MAX_USERS];
    static long[] accountNumbers = new long[MAX_USERS];
    static long[] balances = new long[MAX_USERS];

    static boolean[] firstBonusClaimed =
            new boolean[MAX_USERS];

    static LocalDate[] lastBonusDate =
            new LocalDate[MAX_USERS];

    static int userCount = 0;

    static long nextAccountNumber = 1000000000L;


    // ==========================================
    // MAIN
    // ==========================================

    public static void main(String[] args) throws Exception {

        loadAccounts();

        HttpServer server =
                HttpServer.create(
                        new InetSocketAddress(PORT),
                        0
                );

        server.createContext("/", Main::homePage);

        server.createContext(
                "/login",
                Main::loginPage
        );

        server.createContext(
                "/create",
                Main::createAccountPage
        );

        server.createContext(
                "/dashboard",
                Main::dashboardPage
        );

        server.createContext(
                "/withdraw",
                Main::withdrawPage
        );

        server.createContext(
                "/send",
                Main::sendPage
        );

        server.createContext(
                "/admin",
                Main::adminPage
        );

        server.createContext(
                "/adminDeposit",
                Main::adminDepositPage
        );

        server.createContext(
                "/logout",
                Main::logoutPage
        );

        server.start();

        System.out.println();
        System.out.println("================================");
        System.out.println("          GREEDYBANK");
        System.out.println("================================");
        System.out.println();
        System.out.println(
                "Website started!"
        );
        System.out.println();
        System.out.println(
                "Open this in your browser:"
        );
        System.out.println();
        System.out.println(
                "http://localhost:8080"
        );
        System.out.println();
    }


    // ==========================================
    // HOME PAGE
    // ==========================================

    static void homePage(HttpExchange exchange)
            throws IOException {

        String html = page(
                "GREEDYBANK",
                """
                <div class="card center">
                    <h1>GREEDYBANK</h1>

                    <p class="subtitle">
                        Your Java Banking System
                    </p>

                    <a class="button"
                       href="/login">
                       LOGIN
                    </a>

                    <a class="button secondary"
                       href="/create">
                       CREATE ACCOUNT
                    </a>
                </div>
                """
        );

        send(exchange, html);
    }


    // ==========================================
    // LOGIN PAGE
    // ==========================================

    static void loginPage(HttpExchange exchange)
            throws IOException {

        if (exchange.getRequestMethod()
                .equalsIgnoreCase("POST")) {

            Map<String, String> data =
                    readForm(exchange);

            String name =
                    data.getOrDefault(
                            "name",
                            ""
                    );

            String accountText =
                    data.getOrDefault(
                            "account",
                            ""
                    );

            String password =
                    data.getOrDefault(
                            "password",
                            ""
                    );

            long account;

            try {

                account =
                        Long.parseLong(
                                accountText
                        );

            } catch (Exception e) {

                send(
                        exchange,
                        page(
                                "Login",
                                error(
                                        "Account number must contain numbers."
                                )
                        )
                );

                return;
            }


            // ==================================
            // ADMIN LOGIN
            // ==================================

            if (
                    name.equalsIgnoreCase(
                            ADMIN_NAME
                    )
                    &&
                    password.equals(
                            ADMIN_PASSWORD
                    )
                    &&
                    account == ADMIN_ACCOUNT
            ) {

                String html = page(
                        "Admin Login",
                        """
                        <div class="card center">
                            <h1>Admin Login Successful</h1>

                            <p>
                                Welcome Admin Lohith!
                            </p>

                            <a class="button"
                               href="/admin">
                               ADMIN DASHBOARD
                            </a>
                        </div>
                        """
                );

                send(exchange, html);

                return;
            }


            // ==================================
            // USER LOGIN
            // ==================================

            int index =
                    findUser(
                            name,
                            account,
                            password
                    );


            if (index == -1) {

                send(
                        exchange,
                        page(
                                "Login",
                                error(
                                        "Invalid name, account number, or password."
                                )
                        )
                );

                return;
            }


            // Automatic bonus
            String bonusMessage =
                    giveBonus(index);


            String html = page(
                    "Login Successful",
                    """
                    <div class="card center">
                        <h1>Login Successful!</h1>

                        <p>
                            Welcome, <b>%s</b>
                        </p>

                        <div class="bonus">
                            %s
                        </div>

                        <a class="button"
                           href="/dashboard?account=%d">
                           GO TO DASHBOARD
                        </a>
                    </div>
                    """.formatted(
                            escape(names[index]),
                            bonusMessage,
                            accountNumbers[index]
                    )
            );

            send(exchange, html);

            return;
        }


        String html = page(
                "Login",
                """
                <div class="card">
                    <h1>Login</h1>

                    <form method="POST">

                        <label>Name</label>
                        <input
                            type="text"
                            name="name"
                            required
                        >

                        <label>Account Number</label>
                        <input
                            type="text"
                            name="account"
                            required
                        >

                        <label>Password</label>
                        <input
                            type="password"
                            name="password"
                            required
                        >

                        <button class="button"
                                type="submit">
                            LOGIN
                        </button>

                    </form>

                    <a class="back"
                       href="/">
                       ← Back
                    </a>
                </div>
                """
        );

        send(exchange, html);
    }


    // ==========================================
    // CREATE ACCOUNT
    // ==========================================

    static void createAccountPage(
            HttpExchange exchange
    ) throws IOException {

        if (exchange.getRequestMethod()
                .equalsIgnoreCase("POST")) {

            Map<String, String> data =
                    readForm(exchange);

            String name =
                    data.getOrDefault(
                            "name",
                            ""
                    ).trim();

            String password =
                    data.getOrDefault(
                            "password",
                            ""
                    );


            if (name.isEmpty()
                    || password.isEmpty()) {

                send(
                        exchange,
                        page(
                                "Create Account",
                                error(
                                        "Name and password are required."
                                )
                        )
                );

                return;
            }


            if (userCount >= MAX_USERS) {

                send(
                        exchange,
                        page(
                                "Create Account",
                                error(
                                        "Maximum accounts reached."
                                )
                        )
                );

                return;
            }


            // Check duplicate name
            for (int i = 0;
                 i < userCount;
                 i++) {

                if (
                        names[i]
                                .equalsIgnoreCase(
                                        name
                                )
                ) {

                    send(
                            exchange,
                            page(
                                    "Create Account",
                                    error(
                                            "That name is already being used."
                                    )
                            )
                    );

                    return;
                }
            }


            long account =
                    createUniqueAccountNumber();


            names[userCount] = name;
            passwords[userCount] = password;
            accountNumbers[userCount] = account;
            balances[userCount] = 0;

            firstBonusClaimed[userCount] =
                    false;

            lastBonusDate[userCount] =
                    null;

            userCount++;

            saveAccounts();


            String html = page(
                    "Account Created",
                    """
                    <div class="card center">

                        <h1>Account Created!</h1>

                        <p>
                            Welcome to GREEDYBANK.
                        </p>

                        <div class="account-box">

                            <p>
                                Name
                            </p>

                            <h2>%s</h2>

                            <p>
                                Your Account Number
                            </p>

                            <h2>%d</h2>

                            <p>
                                Starting Balance
                            </p>

                            <h2>○0 Units</h2>

                        </div>

                        <p>
                            <b>
                            Save your account number.
                            </b>
                        </p>

                        <a class="button"
                           href="/login">
                           LOGIN
                        </a>

                    </div>
                    """.formatted(
                            escape(name),
                            account
                    )
            );

            send(exchange, html);

            return;
        }


        String html = page(
                "Create Account",
                """
                <div class="card">

                    <h1>Create Account</h1>

                    <form method="POST">

                        <label>Your Name</label>

                        <input
                            type="text"
                            name="name"
                            required
                        >

                        <label>Create Password</label>

                        <input
                            type="password"
                            name="password"
                            required
                        >

                        <button
                            class="button"
                            type="submit">
                            CREATE ACCOUNT
                        </button>

                    </form>

                    <a class="back"
                       href="/">
                       ← Back
                    </a>

                </div>
                """
        );

        send(exchange, html);
    }


    // ==========================================
    // USER DASHBOARD
    // ==========================================

    static void dashboardPage(
            HttpExchange exchange
    ) throws IOException {

        Map<String, String> query =
                readQuery(
                        exchange.getRequestURI()
                                .getRawQuery()
                );

        long account =
                parseLong(
                        query.get("account"),
                        -1
                );

        int index =
                findAccount(account);


        if (index == -1) {

            send(
                    exchange,
                    page(
                            "Error",
                            error(
                                    "Account not found."
                            )
                    )
            );

            return;
        }


        String html = page(
                "Dashboard",
                """
                <div class="card">

                    <h1>GREEDYBANK</h1>

                    <h2>
                        Welcome, %s
                    </h2>

                    <div class="balance">

                        <small>Your Balance</small>

                        <strong>
                            %s
                        </strong>

                    </div>

                    <a class="button"
                       href="/withdraw?account=%d">
                       WITHDRAW MONEY
                    </a>

                    <a class="button"
                       href="/send?account=%d">
                       SEND MONEY
                    </a>

                    <div class="account-box">

                        <p>
                            Account Number
                        </p>

                        <b>%d</b>

                    </div>

                    <a class="back"
                       href="/logout">
                       Logout
                    </a>

                </div>
                """.formatted(
                        escape(names[index]),
                        formatMoney(
                                balances[index]
                        ),
                        account,
                        account,
                        account
                )
        );

        send(exchange, html);
    }


    // ==========================================
    // WITHDRAW
    // ==========================================

    static void withdrawPage(
            HttpExchange exchange
    ) throws IOException {

        Map<String, String> query =
                readQuery(
                        exchange.getRequestURI()
                                .getRawQuery()
                );

        long account =
                parseLong(
                        query.get("account"),
                        -1
                );

        int index =
                findAccount(account);


        if (index == -1) {

            send(
                    exchange,
                    errorPage(
                            "Account not found."
                    )
            );

            return;
        }


        if (exchange.getRequestMethod()
                .equalsIgnoreCase("POST")) {

            Map<String, String> data =
                    readForm(exchange);

            long amount =
                    parseLong(
                            data.get("amount"),
                            -1
                    );


            if (amount <= 0) {

                send(
                        exchange,
                        page(
                                "Withdraw",
                                error(
                                        "Enter a valid amount."
                                )
                        )
                );

                return;
            }


            if (amount >
                    balances[index]) {

                send(
                        exchange,
                        page(
                                "Withdraw",
                                error(
                                        "Insufficient balance."
                                )
                        )
                );

                return;
            }


            balances[index] -= amount;

            saveAccounts();


            send(
                    exchange,
                    page(
                            "Withdraw Successful",
                            """
                            <div class="card center">

                                <h1>Withdrawal Successful</h1>

                                <p>
                                    Withdrawn:
                                </p>

                                <h2>
                                    %s
                                </h2>

                                <p>
                                    New Balance:
                                </p>

                                <h2>
                                    %s
                                </h2>

                                <a class="button"
                                   href="/dashboard?account=%d">
                                   BACK TO DASHBOARD
                                </a>

                            </div>
                            """.formatted(
                                    formatMoney(
                                            amount
                                    ),
                                    formatMoney(
                                            balances[index]
                                    ),
                                    account
                            )
                    )
            );

            return;
        }


        String html = page(
                "Withdraw",
                """
                <div class="card">

                    <h1>Withdraw Money</h1>

                    <p>
                        Current Balance:
                    </p>

                    <h2>
                        %s
                    </h2>

                    <form method="POST">

                        <label>
                            Amount
                        </label>

                        <input
                            type="number"
                            name="amount"
                            min="1"
                            required
                        >

                        <button
                            class="button"
                            type="submit">
                            WITHDRAW
                        </button>

                    </form>

                    <a class="back"
                       href="/dashboard?account=%d">
                       ← Back
                    </a>

                </div>
                """.formatted(
                        formatMoney(
                                balances[index]
                        ),
                        account
                )
        );

        send(exchange, html);
    }


    // ==========================================
    // SEND MONEY
    // ==========================================

    static void sendPage(
            HttpExchange exchange
    ) throws IOException {

        Map<String, String> query =
                readQuery(
                        exchange.getRequestURI()
                                .getRawQuery()
                );

        long senderAccount =
                parseLong(
                        query.get("account"),
                        -1
                );

        int sender =
                findAccount(
                        senderAccount
                );


        if (sender == -1) {

            send(
                    exchange,
                    errorPage(
                            "Account not found."
                    )
            );

            return;
        }


        if (exchange.getRequestMethod()
                .equalsIgnoreCase("POST")) {

            Map<String, String> data =
                    readForm(exchange);

            long receiverAccount =
                    parseLong(
                            data.get(
                                    "receiver"
                            ),
                            -1
                    );

            long amount =
                    parseLong(
                            data.get("amount"),
                            -1
                    );


            int receiver =
                    findAccount(
                            receiverAccount
                    );


            if (receiver == -1) {

                send(
                        exchange,
                        page(
                                "Send Money",
                                error(
                                        "Receiver account not found."
                                )
                        )
                );

                return;
            }


            if (receiver == sender) {

                send(
                        exchange,
                        page(
                                "Send Money",
                                error(
                                        "You cannot send money to yourself."
                                )
                        )
                );

                return;
            }


            if (amount <= 0) {

                send(
                        exchange,
                        page(
                                "Send Money",
                                error(
                                        "Enter a valid amount."
                                )
                        )
                );

                return;
            }


            if (
                    amount >
                    balances[sender]
            ) {

                send(
                        exchange,
                        page(
                                "Send Money",
                                error(
                                        "Insufficient balance."
                                )
                        )
                );

                return;
            }


            balances[sender] -= amount;

            balances[receiver] += amount;

            saveAccounts();


            send(
                    exchange,
                    page(
                            "Money Sent",
                            """
                            <div class="card center">

                                <h1>Money Sent!</h1>

                                <p>
                                    Sent:
                                </p>

                                <h2>
                                    %s
                                </h2>

                                <p>
                                    To Account:
                                    %d
                                </p>

                                <p>
                                    Your New Balance:
                                </p>

                                <h2>
                                    %s
                                </h2>

                                <a class="button"
                                   href="/dashboard?account=%d">
                                   BACK TO DASHBOARD
                                </a>

                            </div>
                            """.formatted(
                                    formatMoney(
                                            amount
                                    ),
                                    receiverAccount,
                                    formatMoney(
                                            balances[sender]
                                    ),
                                    senderAccount
                            )
                    )
            );

            return;
        }


        String html = page(
                "Send Money",
                """
                <div class="card">

                    <h1>Send Money</h1>

                    <p>
                        Your Balance:
                    </p>

                    <h2>
                        %s
                    </h2>

                    <form method="POST">

                        <label>
                            Receiver Account Number
                        </label>

                        <input
                            type="number"
                            name="receiver"
                            required
                        >

                        <label>
                            Amount
                        </label>

                        <input
                            type="number"
                            name="amount"
                            min="1"
                            required
                        >

                        <button
                            class="button"
                            type="submit">
                            SEND MONEY
                        </button>

                    </form>

                    <a class="back"
                       href="/dashboard?account=%d">
                       ← Back
                    </a>

                </div>
                """.formatted(
                        formatMoney(
                                balances[sender]
                        ),
                        senderAccount
                )
        );

        send(exchange, html);
    }


    // ==========================================
    // ADMIN DASHBOARD
    // ==========================================

    static void adminPage(
            HttpExchange exchange
    ) throws IOException {

        StringBuilder accounts =
                new StringBuilder();


        for (int i = 0;
             i < userCount;
             i++) {

            accounts.append(
                    """
                    <div class="account-box">

                        <b>%s</b>

                        <p>
                            Account:
                            %d
                        </p>

                        <p>
                            Balance:
                            %s
                        </p>

                    </div>
                    """.formatted(
                            escape(names[i]),
                            accountNumbers[i],
                            formatMoney(
                                    balances[i]
                            )
                    )
            );
        }


        String html = page(
                "Admin Dashboard",
                """
                <div class="card">

                    <h1>ADMIN DASHBOARD</h1>

                    <a class="button"
                       href="/adminDeposit">
                       DEPOSIT MONEY
                    </a>

                    <h2>
                        All Customer Accounts
                    </h2>

                    %s

                    <a class="back"
                       href="/logout">
                       Logout
                    </a>

                </div>
                """.formatted(
                        accounts
                )
        );

        send(exchange, html);
    }


    // ==========================================
    // ADMIN DEPOSIT
    // ==========================================

    static void adminDepositPage(
            HttpExchange exchange
    ) throws IOException {

        if (exchange.getRequestMethod()
                .equalsIgnoreCase("POST")) {

            Map<String, String> data =
                    readForm(exchange);

            long account =
                    parseLong(
                            data.get("account"),
                            -1
                    );

            long amount =
                    parseLong(
                            data.get("amount"),
                            -1
                    );


            int index =
                    findAccount(account);


            if (index == -1) {

                send(
                        exchange,
                        page(
                                "Admin Deposit",
                                error(
                                        "Account not found."
                                )
                        )
                );

                return;
            }


            if (amount <= 0) {

                send(
                        exchange,
                        page(
                                "Admin Deposit",
                                error(
                                        "Enter a valid amount."
                                )
                        )
                );

                return;
            }


            long before =
                    balances[index];

            balances[index] += amount;

            saveAccounts();


            String html = page(
                    "Deposit Successful",
                    """
                    <div class="card center">

                        <h1>
                            Deposit Successful
                        </h1>

                        <p>
                            Customer:
                            <b>%s</b>
                        </p>

                        <p>
                            Before:
                        </p>

                        <h2>
                            %s
                        </h2>

                        <p>
                            Deposited:
                        </p>

                        <h2>
                            %s
                        </h2>

                        <p>
                            After:
                        </p>

                        <h2>
                            %s
                        </h2>

                        <a class="button"
                           href="/admin">
                           BACK TO ADMIN
                        </a>

                    </div>
                    """.formatted(
                            escape(names[index]),
                            formatMoney(before),
                            formatMoney(amount),
                            formatMoney(
                                    balances[index]
                            )
                    )
            );

            send(exchange, html);

            return;
        }


        String html = page(
                "Admin Deposit",
                """
                <div class="card">

                    <h1>Admin Deposit</h1>

                    <form method="POST">

                        <label>
                            Customer Account Number
                        </label>

                        <input
                            type="number"
                            name="account"
                            required
                        >

                        <label>
                            Amount
                        </label>

                        <input
                            type="number"
                            name="amount"
                            min="1"
                            required
                        >

                        <button
                            class="button"
                            type="submit">
                            DEPOSIT
                        </button>

                    </form>

                    <a class="back"
                       href="/admin">
                       ← Back
                    </a>

                </div>
                """
        );

        send(exchange, html);
    }


    // ==========================================
    // LOGOUT
    // ==========================================

    static void logoutPage(
            HttpExchange exchange
    ) throws IOException {

        send(
                exchange,
                page(
                        "Logout",
                        """
                        <div class="card center">

                            <h1>Logged Out</h1>

                            <p>
                                You have logged out.
                            </p>

                            <a class="button"
                               href="/">
                               GREEDYBANK HOME
                            </a>

                        </div>
                        """
                )
        );
    }


    // ==========================================
    // AUTOMATIC BONUS
    // ==========================================

    static String giveBonus(int index) {

        LocalDate today =
                LocalDate.now();


        // First login
        if (!firstBonusClaimed[index]) {

            long bonus =
                    500_000_000L;

            balances[index] += bonus;

            firstBonusClaimed[index] =
                    true;

            lastBonusDate[index] =
                    today;

            saveAccounts();

            return """
                   <b>First Login Bonus</b><br>
                   ○500 Million Units added!
                   <br>
                   New Balance:
                   %s
                   """.formatted(
                    formatMoney(
                            balances[index]
                    )
            );
        }


        // Already received today's bonus
        if (
                lastBonusDate[index]
                        != null
                &&
                lastBonusDate[index]
                        .equals(today)
        ) {

            return """
                   Today's bonus has
                   already been added.
                   <br>
                   Balance:
                   %s
                   """.formatted(
                    formatMoney(
                            balances[index]
                    )
            );
        }


        long bonus;


        if (
                today.getDayOfWeek()
                        ==
                DayOfWeek.SUNDAY
        ) {

            bonus =
                    5_000_000L;

        } else {

            bonus =
                    1_000_000L;
        }


        balances[index] += bonus;

        lastBonusDate[index] =
                today;

        saveAccounts();


        return """
               <b>Daily Bonus</b><br>
               %s added!
               <br>
               New Balance:
               %s
               """.formatted(
                formatMoney(bonus),
                formatMoney(
                        balances[index]
                )
        );
    }


    // ==========================================
    // UNIQUE ACCOUNT NUMBER
    // ==========================================

    static long createUniqueAccountNumber() {

        while (true) {

            long number =
                    nextAccountNumber;

            nextAccountNumber++;


            if (
                    findAccount(number)
                    == -1
            ) {

                return number;
            }
        }
    }


    // ==========================================
    // FIND USER
    // ==========================================

    static int findUser(
            String name,
            long account,
            String password
    ) {

        for (int i = 0;
             i < userCount;
             i++) {

            if (
                    names[i]
                            .equalsIgnoreCase(name)
                    &&
                    accountNumbers[i]
                            == account
                    &&
                    passwords[i]
                            .equals(password)
            ) {

                return i;
            }
        }

        return -1;
    }


    // ==========================================
    // FIND ACCOUNT
    // ==========================================

    static int findAccount(
            long account
    ) {

        for (int i = 0;
             i < userCount;
             i++) {

            if (
                    accountNumbers[i]
                            == account
            ) {

                return i;
            }
        }

        return -1;
    }


    // ==========================================
    // SAVE ACCOUNTS
    // ==========================================

    static synchronized void saveAccounts() {

        try (
                PrintWriter writer =
                        new PrintWriter(
                                new FileWriter(
                                        FILE_NAME
                                )
                        )
        ) {

            for (int i = 0;
                 i < userCount;
                 i++) {

                String date =
                        lastBonusDate[i]
                                == null
                                ? "null"
                                : lastBonusDate[i]
                                .toString();


                writer.println(
                        names[i]
                        + "|"
                        + passwords[i]
                        + "|"
                        + accountNumbers[i]
                        + "|"
                        + balances[i]
                        + "|"
                        + firstBonusClaimed[i]
                        + "|"
                        + date
                );
            }

        } catch (IOException e) {

            System.out.println(
                    "Error saving accounts: "
                    + e.getMessage()
            );
        }
    }


    // ==========================================
    // LOAD ACCOUNTS
    // ==========================================

    static void loadAccounts() {

        File file =
                new File(FILE_NAME);


        if (!file.exists()) {

            return;
        }


        try (
                Scanner fileScanner =
                        new Scanner(file)
        ) {

            while (
                    fileScanner.hasNextLine()
                    &&
                    userCount < MAX_USERS
            ) {

                String line =
                        fileScanner.nextLine();


                if (line.isBlank()) {
                    continue;
                }


                String[] data =
                        line.split(
                                "\\|",
                                -1
                        );


                if (data.length < 6) {
                    continue;
                }


                names[userCount] =
                        data[0];

                passwords[userCount] =
                        data[1];

                accountNumbers[userCount] =
                        Long.parseLong(
                                data[2]
                        );

                balances[userCount] =
                        Long.parseLong(
                                data[3]
                        );

                firstBonusClaimed[userCount] =
                        Boolean.parseBoolean(
                                data[4]
                        );


                if (
                        data[5]
                                .equals("null")
                ) {

                    lastBonusDate[userCount] =
                            null;

                } else {

                    lastBonusDate[userCount] =
                            LocalDate.parse(
                                    data[5]
                            );
                }


                userCount++;
            }


            // Continue account numbers
            if (userCount > 0) {

                long highest =
                        accountNumbers[0];


                for (int i = 1;
                     i < userCount;
                     i++) {

                    if (
                            accountNumbers[i]
                                    > highest
                    ) {

                        highest =
                                accountNumbers[i];
                    }
                }


                nextAccountNumber =
                        highest + 1;
            }

        } catch (Exception e) {

            System.out.println(
                    "Error loading accounts: "
                    + e.getMessage()
            );
        }
    }


    // ==========================================
    // FORM READER
    // ==========================================

    static Map<String, String> readForm(
            HttpExchange exchange
    ) throws IOException {

        InputStream input =
                exchange.getRequestBody();


        String body =
                new String(
                        input.readAllBytes(),
                        StandardCharsets.UTF_8
                );


        return readQuery(body);
    }


    // ==========================================
    // QUERY READER
    // ==========================================

    static Map<String, String> readQuery(
            String query
    ) {

        Map<String, String> map =
                new HashMap<>();


        if (
                query == null
                ||
                query.isEmpty()
        ) {

            return map;
        }


        String[] pairs =
                query.split("&");


        for (String pair : pairs) {

            String[] parts =
                    pair.split(
                            "=",
                            2
                    );


            String key =
                    decode(
                            parts[0]
                    );

            String value =
                    parts.length > 1
                            ? decode(parts[1])
                            : "";


            map.put(
                    key,
                    value
            );
        }


        return map;
    }


    // ==========================================
    // URL DECODE
    // ==========================================

    static String decode(
            String value
    ) {

        try {

            return URLDecoder.decode(
                    value,
                    StandardCharsets.UTF_8
            );

        } catch (Exception e) {

            return "";
        }
    }


    // ==========================================
    // SEND HTML
    // ==========================================

    static void send(
            HttpExchange exchange,
            String html
    ) throws IOException {

        byte[] bytes =
                html.getBytes(
                        StandardCharsets.UTF_8
                );


        exchange.getResponseHeaders()
                .set(
                        "Content-Type",
                        "text/html; charset=UTF-8"
                );


        exchange.sendResponseHeaders(
                200,
                bytes.length
        );


        OutputStream output =
                exchange.getResponseBody();


        output.write(bytes);

        output.close();
    }


    // ==========================================
    // ERROR PAGE
    // ==========================================

    static String errorPage(
            String message
    ) {

        return page(
                "Error",
                error(message)
        );
    }


    static String error(
            String message
    ) {

        return """
               <div class="card center">

                   <h1>Error</h1>

                   <p class="error">
                       %s
                   </p>

                   <a class="button"
                      href="/">
                      HOME
                   </a>

               </div>
               """.formatted(
                escape(message)
        );
    }


    // ==========================================
    // MONEY FORMAT
    // ==========================================

    static String formatMoney(
            long amount
    ) {

        if (
                amount >=
                1_000_000_000_000L
        ) {

            return "○"
                    + amount /
                    1_000_000_000_000L
                    + " Trillion Units";

        } else if (
                amount >=
                1_000_000_000L
        ) {

            return "○"
                    + amount /
                    1_000_000_000L
                    + " Billion Units";

        } else if (
                amount >=
                1_000_000L
        ) {

            return "○"
                    + amount /
                    1_000_000L
                    + " Million Units";

        } else if (
                amount >=
                1_000L
        ) {

            return "○"
                    + amount /
                    1_000L
                    + " Thousand Units";

        } else {

            return "○"
                    + amount
                    + " Units";
        }
    }


    // ==========================================
    // PARSE NUMBER
    // ==========================================

    static long parseLong(
            String value,
            long defaultValue
    ) {

        try {

            return Long.parseLong(value);

        } catch (Exception e) {

            return defaultValue;
        }
    }


    // ==========================================
    // ESCAPE HTML
    // ==========================================

    static String escape(
            String text
    ) {

        if (text == null) {
            return "";
        }

        return text
                .replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\"", "&quot;");
    }


    // ==========================================
    // WEBSITE DESIGN
    // ==========================================

    static String page(
            String title,
            String body
    ) {

        return """
               <!DOCTYPE html>

               <html>

               <head>

                   <meta charset="UTF-8">

                   <meta name="viewport"
                         content="width=device-width,
                         initial-scale=1.0">

                   <title>
                       %s - GREEDYBANK
                   </title>

                   <style>

                       * {
                           box-sizing: border-box;
                       }

                       body {
                           margin: 0;
                           font-family: Arial, sans-serif;
                           background: #f4f6f8;
                           color: #222;
                       }

                       .top {
                           background: #111;
                           color: white;
                           padding: 20px;
                           text-align: center;
                       }

                       .top h1 {
                           margin: 0;
                           letter-spacing: 2px;
                       }

                       .container {
                           max-width: 700px;
                           margin: 50px auto;
                           padding: 20px;
                       }

                       .card {
                           background: white;
                           padding: 35px;
                           border-radius: 15px;
                           box-shadow:
                               0 5px 20px
                               rgba(0,0,0,0.10);
                       }

                       .center {
                           text-align: center;
                       }

                       h1 {
                           margin-top: 0;
                       }

                       .subtitle {
                           color: #666;
                           margin-bottom: 30px;
                       }

                       label {
                           display: block;
                           margin-top: 18px;
                           margin-bottom: 7px;
                           font-weight: bold;
                       }

                       input {
                           width: 100%;
                           padding: 13px;
                           border: 1px solid #ccc;
                           border-radius: 8px;
                           font-size: 16px;
                       }

                       .button {
                           display: block;
                           width: 100%;
                           padding: 14px;
                           margin-top: 20px;
                           background: #111;
                           color: white;
                           text-decoration: none;
                           border: none;
                           border-radius: 8px;
                           font-size: 16px;
                           font-weight: bold;
                           cursor: pointer;
                           text-align: center;
                       }

                       .button:hover {
                           opacity: 0.85;
                       }

                       .secondary {
                           background: #666;
                       }

                       .back {
                           display: inline-block;
                           margin-top: 25px;
                           color: #333;
                           text-decoration: none;
                       }

                       .balance {
                           padding: 25px;
                           margin: 25px 0;
                           background: #f1f1f1;
                           border-radius: 12px;
                           text-align: center;
                       }

                       .balance small {
                           display: block;
                           color: #666;
                           margin-bottom: 10px;
                       }

                       .balance strong {
                           font-size: 28px;
                       }

                       .bonus {
                           padding: 20px;
                           margin: 20px 0;
                           background: #f1f1f1;
                           border-radius: 10px;
                       }

                       .account-box {
                           margin-top: 20px;
                           padding: 20px;
                           background: #f5f5f5;
                           border-radius: 10px;
                       }

                       .error {
                           color: #b00020;
                           font-weight: bold;
                       }

                   </style>

               </head>

               <body>

                   <div class="top">
                       <h1>GREEDYBANK</h1>
                   </div>

                   <div class="container">

                       %s

                   </div>

               </body>

               </html>
               """.formatted(
                escape(title),
                body
        );
    }
}