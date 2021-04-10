module fibonacci.server.rmi {
    requires java.rmi;

    requires org.slf4j;

    requires fibonacci.server.core;
    requires fibonacci.shared.core;
    requires fibonacci.shared.rmi;
}