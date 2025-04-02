# OpenAI模型通读

官方文档地址(翻墙访问)

```
https://platform.openai.com/docs
```

## 快速启动

### 创建AIP可以并设置环境变量

[Create an API key in the dashboard here](https://platform.openai.com/api-keys), which you’ll use to securely [access the API](https://platform.openai.com/docs/api-reference/authentication). Store the key in a safe location, like a [`.zshrc` file](https://www.freecodecamp.org/news/how-do-zsh-configuration-files-work/) or another text file on your computer. Once you’ve generated an API key, export it as an [environment variable](https://en.wikipedia.org/wiki/Environment_variable) in your terminal.

在这里的指示板中创建一个API密钥，您将使用它来安全地访问API。将密钥存储在安全的位置，例如计算机上的.zshrc文件或其他文本文件。生成API密钥后，将其作为终端中的环境变量导出。

#### Mac/Linux

```
export OPENAI_API_KEY="your_api_key_here"
```

#### Windows

```
setx OPENAI_API_KEY "your_api_key_here"
```



### 发出第一个请求

将OpenAI API密钥导出为环境变量后，就可以发出第一个API请求了。您可以将REST API直接与您选择的HTTP客户端一起使用，或者使用我们的官方sdk之一，如下所示。

#### JavaScript

要在服务器端JavaScript环境（如Node.js、Deno或Bun）中使用OpenAI API，您可以使用TypeScript和JavaScript的官方OpenAI SDK。首先使用npm或你喜欢的包管理器安装SDK：

```shell
#Install the OpenAI SDK with npm
npm install openai
```

安装OpenAI SDK后，创建一个名为example的文件。MJS并将以下示例之一复制到其中：

```javascript
import OpenAI from "openai";
const openai = new OpenAI();

const completion = await openai.chat.completions.create({
    model: "gpt-4o-mini",
    messages: [
        { role: "system", content: "You are a helpful assistant." },
        {
            role: "user",
            content: "Write a haiku about recursion in programming.",
        },
    ],
    store: true,
});

console.log(completion.choices[0].message);
```



#### Python

Install the OpenAI SDK with pip

```shell
pip install openai
```

安装OpenAI SDK后，创建一个名为example.py的文件，并将以下示例之一复制到其中：

Create a human-like response to a prompt

```python
from openai import OpenAI
client = OpenAI()

completion = client.chat.completions.create(
    model="gpt-4o-mini",
    messages=[
        {"role": "system", "content": "You are a helpful assistant."},
        {
            "role": "user",
            "content": "Write a haiku about recursion in programming."
        }
    ]
)

print(completion.choices[0].message)
```

### 旗舰模型

| Reasoning models                                             | GPT models                                                   |
| ------------------------------------------------------------ | ------------------------------------------------------------ |
| [o1](https://platform.openai.com/docs/models#o1)<br/>High intelligence [reasoning model](https://platform.openai.com/docs/guides/reasoning)<br/>高智能推理模型 | [gpt-4.5-preview](https://platform.openai.com/docs/models#gpt-4-5)<br/>Largest GPT model, good for creative tasks and agentic planning<br/>最大的GPT模型，适合创意任务和代理规划 |
| [o3-mini](https://platform.openai.com/docs/models#o3-mini)<br/>Fast, flexible [reasoning model](https://platform.openai.com/docs/guides/reasoning)<br />快速、灵活的推理模型 | [gpt-4o-mini](https://platform.openai.com/docs/models#gpt-4o-mini)<br/>Fastest responses, cost-effective, customizable<br />反应最快，成本效益高，可定制 |



### 模型概述

OpenAI API由一组不同的模型提供支持，这些模型具有不同的功能和价位。您还可以通过微调对我们的模型进行定制。

| Model                                                        | Description                                                  |
| :----------------------------------------------------------- | :----------------------------------------------------------- |
| [GPT models](https://platform.openai.com/docs/models#gpt-4o) | Our fast, versatile, high-intelligence flagship models<br />我们快速，多功能，高智能的旗舰机型 |
| [Reasoning models](https://platform.openai.com/docs/models#o1) | Our o-series reasoning models excel at complex, multi-step tasks<br />我们的o系列推理模型擅长复杂的、多步骤的任务 |
| [GPT-4o Realtime](https://platform.openai.com/docs/models#gpt-4o-realtime) | GPT-4o models capable of realtime text and audio inputs and outputs<br />gpt-4o型号能够实时文本和音频输入和输出 |
| [GPT-4o Audio](https://platform.openai.com/docs/models#gpt-4o-audio) | GPT-4o models capable of audio inputs and outputs via REST API<br />gpt-4o型号能够通过REST API进行音频输入和输出 |
| [DALL·E](https://platform.openai.com/docs/models#dall-e)     | A model that can generate and edit images given a natural language prompt<br />在给定的自然语言提示下，可以生成和编辑图像的模型 |
| [TTS](https://platform.openai.com/docs/models#tts)           | A set of models that can convert text into natural sounding spoken audio<br />一组可以将文本转换为自然声音语音的模型 |
| [Whisper](https://platform.openai.com/docs/models#whisper)   | A model that can convert audio into text<br />一个可以将音频转换为文本的模型 |
| [Embeddings](https://platform.openai.com/docs/models#embeddings) | A set of models that can convert text into a numerical form<br />一组可以将文本转换为数字形式的模型 |
| [Moderation](https://platform.openai.com/docs/models#moderation) | A fine-tuned model that can detect whether text may be sensitive or unsafe<br />可以检测文本是否敏感或不安全的微调模型 |
| [Deprecated](https://platform.openai.com/docs/deprecations)  | A full list of models that have been deprecated along with the suggested replacement<br />已弃用的模型的完整列表以及建议的替换 |



截止2025-02-28

#### GPT-4.5（最新最强大的GPT模型，擅长于需要创造性、开放式思维和对话的任务）

这是GPT-4.5的研究预览，我们最大和最强大的GPT模型。它深刻的世界知识和对用户意图的更好理解使其擅长创意任务和代理策划。它接受文本和图像输入，并产生文本输出（包括结构化输出）。GPT-4.5还支持关键的开发特性，如函数调用、批处理API和流。

GPT-4.5擅长于需要创造性、开放式思维和对话的任务，比如写作、学习或探索新想法。

GPT-4.5型号的知识截止日期为2023年10月。



#### GPT-4o

gpt - 4o（“o”代表“omni”）是我们多功能、高智能的旗舰机型。它接受文本和图像输入，并产生文本输出（包括结构化输出）。在我们的文本生成指南中学习如何使用gpt - 4o。

下面的ChatGPT - 4o -latest model ID连续指向ChatGPT中使用的gpt- 4o版本。当ChatGPT的gpt - 4o模型发生重大变化时，它会经常更新。

gpt - 4o型号的知识截止日期是2023年10月。



#### GPT-4o mini

gpt - 40mini（“o”代表“omni”）是一款快速、经济实惠的小型机型，适用于集中任务。它接受文本和图像输入，并产生文本输出（包括结构化输出）。它非常适合进行微调，并且可以将来自较大模型（如gpt - 40）的模型输出提取到gpt - 40 -mini，以更低的成本和延迟产生类似的结果。

gpt - 40 -mini模型的知识截止日期是2023年10月。



#### o1 and o1-mini

01系列模型使用强化学习进行训练，以执行复杂的推理。O1模型在回答问题之前会先思考，在回应用户之前会产生很长的内部思考链。在我们的推理指南中了解01模型的功能。

o1推理模型旨在解决跨领域的难题。O1-mini是一款速度更快、价格更实惠的推理模型，但我们建议使用更新的o3-mini模型，它在与O1-mini相同的延迟和价格下具有更高的智能。

最新的01模型支持文本和图像输入，并生成文本输出（包括结构化输出）。o1 -mini目前只支持文本输入和输出。

01和01 -mini模型的知识截止日期为2023年10月。



#### o3-mini

O3-mini是我们最新的小型推理模型，在与o1-mini相同的成本和延迟目标下提供高智能。o3-mini还支持关键的开发特性，如结构化输出、函数调用、批处理API等。与o系列中的其他机型一样，它的设计目的是在科学、数学和编码任务方面表现出色。



#### GPT-4o and GPT-4o-mini Realtime（预览版迷你实时模型，可以通过websocket和webrtc接口实时响应）

这是一个预览版的**gpt - 4o**和**gpt - 4o** -迷你实时模型。这些模型能够通过**webbrtc**或**WebSocket**接口实时响应音频和文本输入。在实时API指南中了解更多信息。



#### GPT-4o and GPT-4o-mini Audio

这是gpt - 40音频模型的预览版本。这些模型接受音频输入和输出，并且可以在聊天完成REST API中使用。学习更多的知识。



#### GPT-4 Turbo and GPT-4

GPT-4是高智能GPT模型的旧版本，可用于**聊天完成**。在文本生成指南中了解更多信息。最新的GPT-4 Turbo版本的知识截止日期是2023年12月。



#### GPT-3.5 Turbo

GPT-3.5 Turbo模型可以理解和生成自然语言或代码，并且已经使用[聊天完成API]（https://platform.openai.com/docs/api-reference/chat）对聊天进行了优化，但也可以很好地用于非聊天任务。

到2024年7月，gpt- 4o -mini应该取代gpt-3.5 turbo，因为它更便宜，功能更强大，多模式，速度也一样快。gpt-3.5-turbo仍然可以在API中使用。



#### DALL·E

DALL·E是一个人工智能系统，可以通过自然语言的描述创造逼真的图像和艺术。DALL·E3目前支持在提示下创建具有特定大小的新映像的功能。DALL·E2还支持编辑现有映像，或创建用户提供的映像的变体。

DALL·E3可通过我们的图像API与DALL·E2一起使用。您可以通过ChatGPT Plus尝试DALL·E3。



#### TTS

TTS是一种人工智能模型，可以将文本转换为自然发音的口语文本。我们提供了两种不同的模型变量，ts-1针对实时文本到语音的用例进行了优化，而ts-1-hd针对质量进行了优化。这些模型可以与Audio API中的Speech端点一起使用。



#### Whisper（多语言语音识别以及语音翻译和语言识别）

Whisper是一个通用的语音识别模型。它是在不同音频的大型数据集上训练的，也是一个多任务模型，可以执行多语言语音识别以及语音翻译和语言识别。Whisper v2大型模型目前可通过我们的API使用Whisper -1模型名称。

目前，Whisper的开源版本和通过我们的API提供的版本之间没有区别。然而，通过我们的API，我们提供了一个优化的推理过程，这使得通过我们的API运行Whisper比通过其他方式运行要快得多。关于Whisper的更多技术细节，你可以阅读这篇论文。



#### Embeddings（搜索、聚类、推荐、异常检测和分类任务）

Embeddings文本的数字表示，可用于测量两段文本之间的相关性。Embeddings对于搜索、聚类、推荐、异常检测和分类任务非常有用。您可以在公告博客文章中阅读有关我们最新Embeddings模型的更多信息。



#### Moderation（查找仇恨、自残、性内容、暴力等类别的内容，审核用）

Moderation模型用于检查内容是否符合OpenAI的使用策略。这些模型提供了分类功能，可以查找仇恨、自残、性内容、暴力等类别的内容。在我们的审核指南中了解更多关于审核文本和图像的信息。



#### GPT base

GPT基本模型可以理解并生成自然语言或代码，但不需要按照指令进行训练。这些模型可以替代原有的GPT-3基本模型，并使用传统的完井API。大多数客户应该使用GPT-3.5或GPT-4。



API数据会保留最多30天，之后将被删除，对于具有敏感应用程序的受信任客户，可以使用零数据保留。在零数据保留的情况下，请求和响应主体不会被持久化到任何日志机制中，而只存在于内存中，以便为请求提供服务。



### How we use your data（我们如何使用你的数据）

#### **Chat Completions:**

- Image inputs via the `o1`, `gpt-4.5-preview`, `gpt-4o`, `gpt-4o-mini`, `chatgpt-4o-latest`, or `gpt-4-turbo` models (or previously `gpt-4-vision-preview`) are not eligible for zero retention. （通过o1， gpt-4.5-预览，gpt- 40, gpt- 40 -mini， chatgpt- 40 -latest或gpt-4-turbo模型（或以前的gpt-4-视觉预览）输入的图像不符合零保留条件。）
- Audio outputs are stored for 1 hour to enable [multi-turn conversations](https://platform.openai.com/docs/guides/audio), and are not currently eligible for zero retention.（音频输出存储1小时，以实现多回合对话，目前不符合零保留条件。）
- When Structured Outputs is enabled, schemas provided (either as the `response_format` or in the function definition) are not eligible for zero retention, though the completions themselves are.（当启用结构化输出时，提供的模式（作为response_format或在函数定义中）不符合零保留条件，尽管补全本身符合。）
- When using Stored Completions via the `store: true` option in the API, those completions are stored for 30 days. Completions are stored in an unfiltered form after an API response, so please avoid storing completions that contain sensitive data.（通过 API 中的“store: true”选项使用“存储完成”时，这些完成将存储 30 天。完成在 API 响应后以未过滤的形式存储，因此请避免存储包含敏感数据的完成。）



#### **Assistants API:**

- Objects related to the Assistants API are deleted from our servers 30 days after you delete them via the API or the dashboard. Objects that are not deleted via the API or dashboard are retained indefinitely.（与 Assistants API 相关的对象会在您通过 API 或控制面板删除它们 30 天后从我们的服务器中删除。未通过 API 或控制面板删除的对象将无限期保留。）



#### **Evaluations**

评估数据：当您创建评估时，与该评估相关的数据将在您通过控制面板删除该评估 30 天后从我们的服务器中删除。未通过控制面板删除的评估数据将无限期保留。



### Model endpoint compatibility（模型端点兼容性）

| Endpoint                 | Latest models                                                |
| :----------------------- | :----------------------------------------------------------- |
| /v1/assistants           | 所有的 o-series(o系列), GPT-4.5, all GPT-4o (except `chatgpt-4o-latest`), GPT-4o-mini, GPT-4, and GPT-3.5 Turbo models.检索工具需要 `gpt-4-turbo-preview` (以及后续发布的模型) or `gpt-3.5-turbo-1106` (以及随后的版本). |
| /v1/audio/transcriptions | `whisper-1`                                                  |
| /v1/audio/translations   | `whisper-1`                                                  |
| /v1/audio/speech         | `tts-1`,  `tts-1-hd`                                         |
| /v1/chat/completions     | 所有的 o-series(o系列), GPT-4.5, GPT-4o (except for Realtime preview 除了实时预览), GPT-4o-mini, GPT-4, and GPT-3.5 Turbo models and their dated releases 模型及其发布日期. `chatgpt-4o-latest` dynamic model. [Fine-tuned](https://platform.openai.com/docs/guides/fine-tuning) versions of `gpt-4o`, `gpt-4o-mini`, `gpt-4`, and `gpt-3.5-turbo`. |
| /v1/completions (Legacy) | `gpt-3.5-turbo-instruct`,  `babbage-002`,  `davinci-002`     |
| /v1/embeddings           | `text-embedding-3-small`,  `text-embedding-3-large`,  `text-embedding-ada-002` |
| /v1/fine_tuning/jobs     | `gpt-4o`,  `gpt-4o-mini`,  `gpt-4`,  `gpt-3.5-turbo`         |
| /v1/moderations          | `text-moderation-stable`,  `text-moderation-latest`          |
| /v1/images/generations   | `dall-e-2`,  `dall-e-3`                                      |
| /v1/realtime (beta)      | `gpt-4o-realtime-preview`, `gpt-4o-realtime-preview-2024-10-01` |

# Text generation（文本生成）

OpenAI 提供了简单的 API，可以使用大型语言模型根据提示生成文本，就像使用 ChatGPT 一样。这些模型经过大量数据训练，可以理解多媒体输入和自然语言指令。根据这些提示，模型可以生成几乎任何类型的文本响应，例如代码、数学方程式、结构化 JSON 数据或类似人类的散文。

要生成文本，您可以使用 REST API 中的聊天完成端点，如以下示例所示。您可以使用所选 HTTP 客户端中的 REST API，也可以使用 OpenAI 针对您首选编程语言的官方 SDK 之一。

Create a human-like response to a prompt

javascript

```javascript
import OpenAI from "openai";
const openai = new OpenAI();

const completion = await openai.chat.completions.create({
    model: "gpt-4o",
    messages: [
        { role: "developer", content: "You are a helpful assistant." },
        {
            role: "user",
            content: "Write a haiku about recursion in programming.",
        },
    ],
    store: true,
});

console.log(completion.choices[0].message);
```



### Choosing a model

When making a text generation request, your first decision is which [model](https://platform.openai.com/docs/models) you want to generate the response. The model you choose influences output and impacts [cost](https://openai.com/api/pricing/).（在发出文本生成请求时，您首先要决定要使用哪种模型来生成响应。您选择的模型会影响输出并影响成本。）

- A **large model** like [`gpt-4o`](https://platform.openai.com/docs/models#gpt-4o) offers a very high level of intelligence and strong performance, with higher cost per token.
  像 gpt-4o 这样的大型模型提供了非常高的智能水平和强大的性能，但每个令牌的成本更高。
- A **small model** like [`gpt-4o-mini`](https://platform.openai.com/docs/models#gpt-4o-mini) offers intelligence not quite on the level of the larger model, but it's faster and less expensive per token.
  像 gpt-4o-mini 这样的小型模型提供的智能程度不如大型模型，但速度更快，每个令牌的成本更低。
- A **reasoning model** like [the `o1` family of models](https://platform.openai.com/docs/models#o1) is slower to return a result, and uses more tokens to "think," but is capable of advanced reasoning, coding, and multi-step planning.
  像 o1 系列模型这样的推理模型返回结果的速度较慢，并且使用更多令牌来“思考”，但能够进行高级推理、编码和多步骤规划。

Experiment with different models [in the Playground](https://platform.openai.com/playground) to see which works best for your prompts! You might also benefit from our [model selection best practices](https://platform.openai.com/docs/guides/model-selection). 在 Playground 中尝试不同的模型，看看哪种模型最适合您的提示！您也可能受益于我们的模型选择最佳实践。



### Building prompts（构建提示）

The process of crafting prompts to get the right output from a model is called **prompt engineering**. You can improve output by giving the model precise instructions, examples, and necessary context information—like private or specialized information not included in the model's training data.
构建提示以从模型中获得正确输出的过程称为提示工程。您可以通过向模型提供精确的说明、示例和必要的上下文信息（例如模型训练数据中未包含的私人或专业信息）来改进输出。

Below is high-level guidance on building prompts. For more in-depth strategies and tactics, see the [prompt engineering guide](https://platform.openai.com/docs/guides/prompt-engineering).

以下是构建提示的高级指南。有关更深入的策略和战术，请参阅提示工程指南。



### Messages and roles

In the [chat completions API](https://platform.openai.com/docs/api-reference/chat/), you create prompts by providing an array of `messages` that contain instructions for the model. Each message can have a different `role`, which influences how the model might interpret the input.

在聊天完成 API 中，您可以通过提供包含模型说明的消息数组来创建提示。每条消息可以有不同的角色，这会影响模型解释输入的方式。

| Role                | Description                                                  | Usage example（使用示例）                                    |
| :------------------ | :----------------------------------------------------------- | :----------------------------------------------------------- |
| `user`              | Instructions that request some output from the model. Similar to messages you'd type in [ChatGPT](https://chatgpt.com/) as an end user.<br />请求模型输出某些输出的指令。类似于您作为最终用户在 [ChatGPT](https://chatgpt.com/) 中输入的消息。 | Pass your end-user's message to the model.`Write a haiku about programming.`<br />将最终用户的消息传递给模型。`写一首关于编程的俳句。` |
| `developer`(开发商) | Instructions to the model that are prioritized ahead of user messages, following [chain of command](https://cdn.openai.com/spec/model-spec-2024-05-08.html#follow-the-chain-of-command). Previously called the `system` prompt.<br />按照 [命令链](https://cdn.openai.com/spec/model-spec-2024-05-08.html#follow-the-chain-of-command)，向模型发出优先于用户消息的指令。以前称为 `系统` 提示。 | Describe how the model should generally behave and respond.`1 2 3 4 5 You are a helpful assistant that answers programming questions in the style of a southern belle from the southeast United States.`Now, any response to a `user` message should have a southern belle personality and tone.<br />描述模型通常应如何表现和响应。`1 2 3 4 5 你是一个乐于助人的助手，可以以美国东南部南方美女的风格回答编程问题。`现在，对“用户”消息的任何回复都应该具有南方美女的个性和语气。 |
| `assistant`（助手） | A message generated by the model, perhaps in a previous generation request (see the "Conversations" section below).<br />模型生成的消息，可能在上一次生成的请求中（请参阅下面的“对话”部分）。 | Provide examples to the model for how it should respond to the current request.For example, to get a model to respond correctly to knock-knock jokes, you might provide a full back-and-forth dialogue of a knock-knock joke.<br />向模型提供应如何响应当前请求的示例。例如，为了让模型正确响应敲门笑话，您可以提供敲门笑话的完整来回对话。 |

Message roles may help you get better responses, especially if you want a model to follow hierarchical instructions. They're not deterministic, so the best way to use them is just trying things and seeing what gives you good results.

消息角色可能有助于您获得更好的响应，特别是如果您希望模型遵循分层指令时。它们不是确定性的，因此使用它们的最佳方式就是尝试并查看哪些方法可以带来良好的结果。



Here's an example of a developer message that modifies the behavior of the model when generating a response to a `user` message:

以下是开发人员消息的示例，它在生成对用户消息的响应时修改了模型的行为：

```javascript
const response = await openai.chat.completions.create({
  model: "gpt-4o",
  messages: [
    {
      "role": "developer",
      "content": [
        {
          "type": "text",
          "text": `
            You are a helpful assistant that answers programming 
            questions in the style of a southern belle from the 
            southeast United States. 
          ` //你是一个乐于助人的助手，以美国东南部南方美女的风格回答编程问题。
        }
      ]
    },
    {
      "role": "user",
      "content": [
        {
          "type": "text",
          "text": "Are semicolons optional in JavaScript?" //JavaScript 中的分号是可选的吗
        }
      ]
    }
  ],
  store: true,
});
```



This prompt returns a text output in the rhetorical style requested:

此提示将返回按照所要求的修辞风格输出的文本：

```text
Well, sugar, that's a fine question you've got there! Now, in the 
world of JavaScript, semicolons are indeed a bit like the pearls 
on a necklace – you might slip by without 'em, but you sure do look 
more polished with 'em in place. 
好吧，亲爱的，你这个问题问得真好！现在，在 JavaScript 的世界里，分号确实有点像项链上的珍珠——没有它们，你可能会被忽略，但有了它们，你肯定会看起来更精致。

Technically, JavaScript has this little thing called "automatic 
semicolon insertion" where it kindly adds semicolons for you 
where it thinks they oughta go. However, it's not always perfect, 
bless its heart. Sometimes, it might get a tad confused and cause 
all sorts of unexpected behavior.
从技术上讲，JavaScript 有一个称为“自动插入分号”的小功能，它会在它认为应该出现的位置为你添加分号。然而，它并不总是完美的，愿上帝保佑它。有时，它可能会有点困惑，并导致各种意外行为。
```





















































































