# LangChain、LangGraph入门

## LangChain和LangGraph基础知识入门

参考网址

```
https://blog.csdn.net/Javachichi/article/details/149065223
```



### 概念

#### LangChain

使用LangChain主要有两种方式：**预定义命令的顺序链（Chain）和LangChain Agent。**这两种方式在工具和编排方式上有所不同。链采用预定义的线性工作流，而Agent则充当一个协调者，可以进行更具有动态性（非线性）的决策。

- Chain（链）：一系列步骤的组合，这些步骤包括可以调用LLM、Agent、工具、外部数据源、过程式代码等等，即根据逻辑条件将单一流程拆分成多个路径。
- Agent或LLM：LLM本身就是能够生成自然语言响应，而Agent则结合了LLM与其他额外的能力，能够进行推理、工具调用，并且在调用工具失败的时候重试。
- Tool（工具）：是可以再链中被调用的代码函数，或者有Agent触发，与外部系统交互
- Prompt（提示词）：包括系统提示词（用于指示模型如何完成任务以及可用工具）、来自外部数据源的注入信息（为模型提供更多的上下文）、以及任务输入的任务信息。

#### LangGraph

LangGraph采用了一种特殊的方式来构建AI的工作流，**他是以图Graph的方式编排AI工作流**。由于其在AI Agent、过程式代码和其他工具之间的灵活处理能力，他更适用于线性链、分支链或者简单的Agent系统难以满足的复杂应用场景。

LangGraph设计用于处理更复杂的条件逻辑和反馈循环，比LangChain更加的强大。

- Graph（图）是一种灵活的工作流组织方式，支持调用LLM、工具、外部数据源、过程代码等等，LangGraph还支持循环图（Cyclical Graph），就是可以创建循环和反馈机制，让某些节点可以被多次访问。
- Node（节点）表示工作流中的步骤，例如LLM查询、Api调用或者工具执行。
- Edge（边）和Conditional Edge（条件边）：用于连接节点，定义信息流向，是一个节点的信息输出可以作为下一个节点的输入，**条件变允许在满足特定条件的时候，将信息从一个节点流向另外一个节点，开发者可以自定义这些条件**
- State（状态）**表示应用程序的当前状态，随着信息在图中流动而更新，状态是一个开发者自定义的可变的TypedDict对象**，其中包含当前执行图所需要的所有信息，LangGraph会在每个节点自动更新状态。
- Agent或LLM：图中的LLM仅负责对输入生成文本响应。**而Agent能力则允许图中包含多个节点，分别代码Agent的不同组件（如推理、工具选择和工具执行）**。Agent可以**决定在图中执行采取哪条路径、更新图的状态、并且执行比单纯文本生成更多任务**

相比之下，**LangChain更加适合线性的和基于工具的调用，而LangGraph更加适合复杂的、多路径和具有反馈机制的AI工作流**

#### 两个框架在核心功能处理方式上的区别

LangGraph和LangChain在某些能力上有所重叠，但是他们处理问题的方式是不同的。**LangGraph主要关注线性的工作流（通过链）或不同的AIAgent模式，而LangGraph则专注于创建更加灵活、细粒度的、基于流程的工作流，**其中可以包含AIAgent、工具调用、过程式代码等等。



### 工具的调用（Tool Calling）

#### LangChain

在LangChain中，工具的调用方式取决于是**在链中按顺序执行一系列步骤，还是仅仅使用Agent能力（不在链中明确定义）**

- 在链中，工具是作为预定义步骤包含的，这意味着他并不一定是由Agent动态调用的，而是在链设计的时候就已经决定了调用那些工具
- 当Agent不在链中定义的时候，Agent具有更加强大的自主性，他可以根据自己访问的工具列表，**决定调用哪个工具以及何时调用**



**链方式的流程示例**

![image-20251129150536291](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20251129150536291.png) 

**Agent方式的流程示例**

![image-20251129150640524](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20251129150640524.png) 

#### LangGraph

在LangGraph中，工具通常被表示为图上的一个节点。如果图中包含一个Agent，那么Agent负责决定调用哪个工具，这一决策基于其推理能力。当Agent选择某个工具之后，工作流会跳转到对应的“工具节点”（Tool Node），以执行该工具的操作。在Agent和工具节点之间的边可以包含条件逻辑（Conditional Logic），从而增加额外的判断逻辑，以决定是否执行某个工具。这样，开发者就可以用更加精细的控制能力。如果图中没有Agent，那么工具的调用类似于LangChain的链，就是根据预定义的逻辑在工作流中执行工具。

Agent的图流程示例：
![image-20251129164706458](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20251129164706458.png) 

没有Agent的图流程示例：

![image-20251129164742364](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20251129164742364.png) 



### 对话历史的记忆

#### LangChain

LangChain提供**内置的抽象层**来处理对话历史和记忆，他支持不同粒度级别（granularity）的记忆管理，从而控制台传递给LLM的token数量，主要包括以下几种方式：

- 完整的会话历史（Full Session conversation history）
- 摘要版本对话历史（Summarized conversation history）
- 自定义定义的记忆（Customer defined history）

此外，**开发者可以自定义长期记忆系统，**将对话历史存储在**外部数据库** 中，并在需要时**检索相关数据记忆**



#### LangGraph

在LangGraph中，**State（状态）负责管理记忆**，他通过**记录每个时刻定义的变量**来跟踪状态信息。State可以包括：

- 对话历史
- 任务执行的各个步骤
- 语言模型上一次的输出结果
- 其他重要信息

State **可以在节点之间传递**，这样每个节点都能获取当前系统的状态，然而，**LangGraph本身不提供跨会话的长期记忆功能，**如果开发者需要**持久化记忆**，可以引入特定的节点，用于将记忆的变量存入**外部数据库**，以便后续检索。



### 开箱即用的RAG能力

#### LangChain

LangChain**原生支持**复杂的RAG工作流，并且提供了一套成熟的工具，方便开发者将RAG集成到应用程序中。例如：他提供了：

- 文档加载（Document Loading）
- 文本解析（Text Parsing）
- Embedding生成（Embedding Creators）
- 向量存储（Vector Storage）
- 检索能力（Retrieval Capabilities）



开发者可以直接使用LangChain提供的API（如：` langchain.document_loaders`、`langchain.embeddings和 langchain.vectorstores`）来实现RAG工作流

#### LangGraph

在LangGraph中，**RAG需要开发者自行设计**，并作为图结构的一部分实现。例如开发者可以单独创建节点，分别用于：

- 文档解析（Document Parsing）
- Embedding计算（Embedding Computation）
- 语言检索（Retrieval）

在这些节点之间可以通过普通边（Normal Edges）或条件边（Conditional Edges）进行连接，而各个节点状态可以用户传递信息，以便在RAG流水线的不同步骤之间共享数据。



### 并行处理（Parallelism Process）

#### LangChain

LangChain运行并行执行多个链或多个Agent，可以使用可以使用`RunnableParallel`类来实现基本的并行处理。

但是如果需要更高级的**并行计算或者异步工具调用**，则需要使用Python库（如`asyncio`）自行实现

#### LangGraph













































## 使用LangChain与LangGraph打造AI对话系统

首先创建两个文件夹，服务端和客户端

![image-20251129112950401](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20251129112950401.png) 

创建一个虚拟环境

```
python -m venv venv
```

激活虚拟环境

```
.\venv\Scripts\activate
```

![image-20251129113214060](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20251129113214060.png)

现在虚拟环境已经初始化并且激活，我们创建一个app.ipynb

安装langgraph、LangChain LLM模型 ，核心包、python的dotenv（可以再Jupyter notebook中使用环境变量）、fastapi、安装LangChain社区（可以使用访问很多LangChain社区的工具）

```
pip install langgraph langchain_openai langchain_core python_dotenv fastapi langchain_community
```



























