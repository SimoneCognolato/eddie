// SPDX-FileCopyrightText: 2024 The EDDIE Developers <eddie.developers@fh-hagenberg.at>
// SPDX-License-Identifier: Apache-2.0

package energy.eddie.regionconnector.de.eta.exceptions;

public final class EtaPlusClientConfigExceptions {

    private EtaPlusClientConfigExceptions() {
    }

    public static class SslConfigurationException extends IllegalStateException {
        public SslConfigurationException(String message) {
            super(message);
        }
    }

    public static class InvalidApiBaseUrlException extends IllegalStateException {
        public InvalidApiBaseUrlException(String message) {
            super(message);
        }
    }

    public static class MissingCredentialsException extends IllegalStateException {
        public MissingCredentialsException(String message) {
            super(message);
        }
    }

    public static class InvalidTimeoutConfigurationException extends IllegalStateException {
        public InvalidTimeoutConfigurationException(String message) {
            super(message);
        }
    }
}