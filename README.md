This is an application for having fun and learning about Web3 and interacting with the Ethereum blockchain.

It uses SpringBoot for backend and html,css for UI and htmX for dynamically swapping/refreshing fragments of the page.

It leverages the Web3j library that wraps smart contracts written in Solidity language in Java.

It communicates with the Ethereum blockchain nodes through RPC service providers (INFURA and Alchemy) and aggregates the data of the latest Ethereum block. 

It also leverages Chainlink smart contracts and makes calls to collect the Ethereum price from their on-Chain data oracles (for a blockchain the only truth is onchain!) 

Application at http://68.183.214.175:8080/

To deploy the application yourself, you first need to provide an ethereum node infrastructure RPCProvider API key (RPC like INFURA) inside chain_infra_api_url.properties.

You can also deploy the application in a docker container by running: docker compose up --build 
