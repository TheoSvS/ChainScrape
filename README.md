This is a simple SpringBoot application for having fun and learning about Web3 and interacting with the Ethereum blockchain. 

It leverages the Web3j library that wraps smart contracts written in Solidity language in Java.

It communicates with the Ethereum blockchain nodes through RPC service providers (INFURA and Alchemy) and aggregates the data of the latest Ethereum block. 

It also leverages Chainlink smart contracts and makes calls to collect the Ethereum price from their on-Chain data oracles (for a blockchain the only truth is on-chain!) 

To deploy the application you need to provide an ethereum node infrastructure RPCProvider API key (RPC like INFURA) inside chain_infra_api_url.properties.

Then you can run the application and find it at http://localhost:8080/ 

You can also deploy the containerized application simply by running: docker compose up --build 
