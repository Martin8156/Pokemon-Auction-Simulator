# Pokémon Auction Simulator

A real-time, multi-client auction platform built in Java. Users can create accounts, log in (or browse as a guest), and bid on Pokémon in live auctions. Bids are synchronized across all connected clients in real time, auctions close automatically on a countdown timer or when the buy-now price is met, and all auction state persists in MongoDB Atlas.

## Features

- **Live multi-client bidding** — bids placed by one client are instantly broadcast to every connected client using the Observer pattern
- **User accounts** — sign-up and login backed by a MongoDB user collection, plus a guest mode with view-only access
- **Auction lifecycle management** — a scheduled server task decrements each auction's timer every minute; expired auctions (or bids meeting the buy-now price) are automatically moved to a Closed Bids collection
- **Bid history** — every item tracks its full bid history (bidder, item, amount), viewable from the item detail page
- **Buy-now support** — bids at or above the buy-now price immediately win and close the auction
- **Closed auctions view** — browse ended auctions and see final sale prices and bid histories


**Client** (`src/client`)
- `Client.java` — JavaFX application entry point; manages the socket connection and four worker threads (reader, bid writer, login writer, sign-up writer) that communicate through `BlockingQueue`s so the UI never blocks on network I/O
- `LoginScreenController` / `SignUpController` — FXML controllers for authentication flows
- `UIController` — renders the auction grid, item detail pages, bid placement, and closed-bid views

**Server** (`src/server`)
- `Server.java` — accepts client connections, processes bid/login/sign-up requests, persists all state to MongoDB, and runs a scheduled task that ticks auction timers down every 60 seconds
- `ServerClientHandler.java` — one thread per connected client; implements `Observer` so that any state change on the server is pushed to every client automatically

**Shared** (`src/sharedClass`)
- `AuctionItem`, `BidInfo`, `LoginInfo` — data models serialized to JSON with Gson for transport over the socket

## Tech Stack

| Layer | Technology |
|---|---|
| Language | Java |
| UI | JavaFX + FXML |
| Networking | Java TCP sockets, thread-per-client |
| Concurrency | `BlockingQueue`, `ScheduledExecutorService`, Observer pattern |
| Serialization | Gson (JSON) |
| Database | MongoDB Atlas (Java driver) |

## Getting Started

### Prerequisites
- JDK 17+ (with JavaFX SDK if not bundled)
- A MongoDB Atlas cluster (free tier works) with a database containing three collections: `Available Pokemon`, `CustomerLoginInfo`, and `Closed Bids`
- Gson and MongoDB Java driver on the classpath

## What I Learned
- Designing a client-server protocol from scratch (message-type prefixes + JSON payloads)
- Managing concurrency between a UI thread and network threads using blocking queues, and safely updating JavaFX components from background threads with `Platform.runLater`
- Applying the Observer pattern to broadcast server state changes to many clients
- Integrating a cloud database (MongoDB Atlas) and modeling nested documents (bid histories embedded in auction items)
