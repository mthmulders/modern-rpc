#!/usr/bin/env bash
set -euo pipefail

echo RMI registry starting...
rmiregistry -J-Djava.rmi.server.codebase=file:///`pwd`/server/assembly/target/server-assembly-0.1-SNAPSHOT.jar