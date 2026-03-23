# Connection status messages

Connection status messages are an EDDIE internal message format and are an alternative version to the permission market documents.
They provide information about the status change of a permission request.
The JSON schema and XSD files can be found [here](https://github.com/eddie-energy/eddie/tree/main/cim/src/main/schemas/agnostic).

```mermaid
classDiagram
    class ConnectionStatusMessage {
        +String connectionId
        +UUID permissionId
        +UUID dataNeedId
        +DataSourceInformation dataSourceInformation
        +String timestamp
        +String status
        +String? message
        +List~KeyValuePair~ extension
    }

    class DataSourceInformation {
        +String countryCode
        +String meteredDataAdministratorId
        +String permissionAdministratorId
        +String regionConnectorId
    }

    class KeyValuePair {
        +String key
        +String value
    }
    
    ConnectionStatusMessage "1" --> "1" DataSourceInformation: contains
    ConnectionStatusMessage "1" --> "0..*" KeyValuePair: contains
```

