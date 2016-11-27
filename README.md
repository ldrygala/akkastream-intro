# Akka Streams
## Principle of Least Power
* scala.concurrent.Future[T]
    - single value 
    - local abstraction
    - execution context
* AkkaStreams
    - stream of values
    - well typed
    - local abstraction
    - back-pressured
* AkkaTyped
    - experimental
    - well typed
    - location transparent
* AkkaActors
    - location transparent
    - untyped   
## Source <small>*exactly one output*</small>
* Single
* Iterable
* Iterator
* Future
* Tick
* ActorRef 
* ActorPublisher
## Sink <small>*exactly one input*</small>
* Head
* Fold
* Ignore
* Foreach
* ActorRef
    - `OverflowStrategy`
        - `dropHead` (drops the oldest element from buffer)
        - `dropTail` (drops the youngest element from buffer)
        - `dropBuffer` (drops all the buffered elements)
        - `dropNew` (drops the new element)
        - `backpressure` (backpressures the upstream publisher but is not supported for ActorRef)
* ActorSubscriber
    - `RequestStrategy`
        - `OneByOneRequestStrategy` (max one element in flight)
        - `ZeroRequestStrategy` (controlled with manual calls)
        - `WatermarkRequestStrategy` (request up to `highWatermark` when the `remainingRequested` is below the `lowWatermark`)
        - `MaxInFlightRequestStrategy` (request up to the `max` and also takes the number of messages that have been queued internally or delegated to other actors)
## Flow <small>*exactly one input and output*</small>
* Map
* Filter
* MapConcat <small>*flatMap without for comprehension*</small>
* Throttle <small>*speed limited to `elements/per`*</small>
    - `ThrottleMode`
        - `Shaping` <small>*makes pauses before emitting messages*</small>
        - `Enforcing` <small>*fails with exception when upstream is faster than throttle rate*</small>
* Expand <small>*allows faster downstream to progress independently of a slower publisher*</small>
* Conflate <samll>*allows faster upstream to progress independently of slower subscriber*</small>
* GroupBy <samll>*returns `SubFlow`*</small>
## Buffers
* Asynchronous Boundary `async`
* OverflowStrategy
## Thread
* Fusing <small>*put all computations of a graph (where possible) in the same Actor*</small>
* Async <small>*create async boundary*</small>
## Supervisioning
* `Supervision.Decider`
    - Stop 
    - Resume
    - Restart
## Testing
* TestSource
* TestSink
## Graph
* Fan-in
    - `Merge`
        - *(N inputs , 1 output) picks randomly from inputs pushing them one by one to its output*
    - `MergePreferred`
        - *like `Merge` but if elements are available on `preferred` port, it picks from it, otherwise randomly from others*
    - `ZipWith`
        - *(N inputs, 1 output) which takes a function of N inputs that given a value for each input emits 1 output element*
    - `Zip`
        - *(2 inputs, 1 output) is a ZipWith specialised to zipping input streams of A and B into an (A,B) tuple stream*
    - `Concat`
        - *(2 inputs, 1 output) concatenates two streams (first consume one, then the second one)*
* Fan-out
    - `Broadcast[T]` 
        - *(1 input, N outputs) given an input element emits to each output*
    - `Balance[T]`
        - *(1 input, N outputs) given an input element emits to one of its output ports*
    - `UnZip[A,B]`
        - *(1 input, 2 outputs) splits a stream of `(A,B)` tuples into two streams, one of type `A` and one of type `B`*
    - `UnzipWith[In,A,B,...]`
        - *(1 input, N outputs) takes a function of 1 input that given a value for each input emits N output elements (where N <= 20)*
## GraphStage
* Immutable
* GraphStageLogic 
    - single thread
    - beware of Future callbacks