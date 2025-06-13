# FunAsr部署开发

github仓库开源地址

```
https://github.com/modelscope/FunASR/tree/main
```

## 快速上手

### docker安装

如果您已安装docker，忽略本步骤！! 通过下述命令在服务器上安装docker：

```
curl -O https://isv-data.oss-cn-hangzhou.aliyuncs.com/ics/MaaS/ASR/shell/install_docker.sh；
sudo bash install_docker.sh
```

docker安装失败请参考 [Docker Installation](https://alibaba-damo-academy.github.io/FunASR/en/installation/docker.html)

### 镜像启动

通过下述命令拉取并启动FunASR软件包的docker镜像：

```sh
sudo docker pull \
  registry.cn-hangzhou.aliyuncs.com/funasr_repo/funasr:funasr-runtime-sdk-cpu-0.4.6
  
mkdir -p ./funasr-runtime-resources/models

sudo docker run -p 10095:10095 -it --privileged=true \
  --name funasr_service\
  -v $PWD/funasr-runtime-resources/models:/workspace/models \
  registry.cn-hangzhou.aliyuncs.com/funasr_repo/funasr:funasr-runtime-sdk-cpu-0.4.6
```

以容器名称进入此容器（后续维护方便）：

```
docker exec -it funasr_service bash
```



### 服务端启动

docker启动之后，进入到docker里边启动 funasr-wss-server服务程序：

```sh
cd FunASR/runtime
#后台运行
nohup bash run_server_2pass.sh \
  --download-model-dir /workspace/models \
  --certfile 0 \
  --vad-dir damo/speech_fsmn_vad_zh-cn-16k-common-onnx \
  --model-dir damo/speech_paraformer-large-vad-punc_asr_nat-zh-cn-16k-common-vocab8404-onnx  \
  --online-model-dir damo/speech_paraformer-large_asr_nat-zh-cn-16k-common-vocab8404-online-onnx \
  --punc-dir damo/punc_ct-transformer_cn-en-common-vocab471067-large-onnx \
  --lm-dir damo/speech_ngram_lm_zh-cn-ai-wesp-fst \
  --itn-dir thuduj12/fst_itn_zh \
  --hotword /workspace/models/hotwords.txt > log.txt 2>&1 &
  

#非后台运行
bash run_server_2pass.sh \
  --download-model-dir /workspace/models \
  --certfile 0 \
  --vad-dir damo/speech_fsmn_vad_zh-cn-16k-common-onnx \
  --model-dir damo/speech_paraformer-large-vad-punc_asr_nat-zh-cn-16k-common-vocab8404-onnx  \
  --online-model-dir damo/speech_paraformer-large_asr_nat-zh-cn-16k-common-vocab8404-online-onnx \
  --punc-dir damo/punc_ct-transformer_cn-en-common-vocab471067-large-onnx \
  --lm-dir damo/speech_ngram_lm_zh-cn-ai-wesp-fst \
  --itn-dir thuduj12/fst_itn_zh \
  --hotword /workspace/models/hotwords.txt

## 商业转载请联系作者获得授权，非商业转载请注明出处。
# For commercial use, please contact the author for authorization. For non-commercial use, please indicate the source.
# 协议(License)：署名-非商业性使用-相同方式共享 4.0 国际 (CC BY-NC-SA 4.0)
# 作者(Author)：lukeewin
# 链接(URL)：https://blog.lukeewin.top/archives/install-funasr-with-docker#toc-head-6
# 来源(Source)：lukeewin的博客

# 在线模型
#  --online-model-dir /workspace/models/damo/speech_paraformer-large_asr_nat-zh-cn-16k-common-vocab8404-online-onnx \
# 如果您想关闭ssl，增加参数：--certfile 0
# 如果您想使用SenseVoiceSmall模型、时间戳、nn热词模型进行部署，请设置--model-dir为对应模型：
#   iic/SenseVoiceSmall-onnx
#   damo/speech_paraformer-large-vad-punc_asr_nat-zh-cn-16k-common-vocab8404-onnx（时间戳）
#   damo/speech_paraformer-large-contextual_asr_nat-zh-cn-16k-common-vocab8404-onnx（nn热词）
# 如果您想在服务端加载热词，请在宿主机文件./funasr-runtime-resources/models/hotwords.txt配置热词（docker映射地址为/workspace/models/hotwords.txt）:
#   每行一个热词，格式(热词 权重)：阿里巴巴 20（注：热词理论上无限制，但为了兼顾性能和效果，建议热词长度不超过10，个数不超过1k，权重1~100）
# SenseVoiceSmall-onnx识别结果中“<|zh|><|NEUTRAL|><|Speech|> ”分别为对应的语种、情感、事件信息


cd /workspace/FunASR/runtime/websocket/build/bin
#后台运行在线asr模型
nohup ./funasr-wss-server-2pass 
	--vad-dir damo/speech_fsmn_vad_zh-cn-16k-common-onnx    
    --model-dir damo/speech_paraformer-large_asr_nat-zh-cn-16k-common-vocab8404-onnx   
    --online-model-dir damo/speech_paraformer-large_asr_nat-zh-cn-16k-common-vocab8404-online-onnx  
    --punc-dir damo/punc_ct-transformer_zh-cn-common-vad_realtime-vocab272727-onnx > online_funasr.log 2>&1 &
   
cd /workspace/FunASR/runtime/http/bin





```





### 搭建API服务

要实现你需要的功能，整体步骤如下：

### 一、将立体声音频（双通道）左右声道分离

首先，我们需要实现双通道音频到两个单通道音频的拆分。请通过上传方式提供你的音频文件，我们将使用Python进行拆分。

### 二、使用FunASR模型识别拆分后的单通道音频

拆分成为单通道后，我们可以使用FunASR模型做语音内容识别。推荐使用较准确且通用的中文识别模型，如：

- `damo/speech_paraformer-large_asr_nat-zh-cn-16k-common-vocab8404-pytorch` (中文通用识别模型)
- `damo/speech_paraformer-large-vad-punc_asr_nat-zh-cn-16k-common-vocab8404-onnx` (自带VAD和标点恢复功能的模型)

如果你的需求中有长音频处理和端点检测，可以再搭配`vad-dir`模型，比如：

- `damo/speech_fsmn_vad_zh-cn-16k-common-onnx`

### 三、构建API服务，实现上传并返回识别结果

接下来我们利用Python的FastAPI搭建API，通过HTTP请求上传文件进行识别后返回识别结果，更便于后续直接调用。

```python
from fastapi import FastAPI, File, UploadFile
from fastapi.responses import JSONResponse
import os
import uuid
from pydub import AudioSegment
from funasr import AutoModel
import shutil

app = FastAPI()

# 加载你想用的FunASR模型（推荐最佳效果）
model_dir = "damo/speech_paraformer-large-vad-punc_asr_nat-zh-cn-16k-common-vocab8404-onnx"
asr_model = AutoModel(model=model_dir)

@app.post("/recognize_stereo/")
async def recognize_stereo_audio(file: UploadFile = File(...)):
    # 生成唯一的临时文件名
    temp_filename = f"temp_{uuid.uuid4().hex}.wav"
    
    # 保存上传的文件
    with open(temp_filename, 'wb') as buffer:
        shutil.copyfileobj(file.file, buffer)

    try:
        # 使用pydub加载wav音频并拆分立体声为左右声道
        audio = AudioSegment.from_wav(temp_filename)
        left_channel = audio.split_to_mono()[0]  # 左声道
        right_channel = audio.split_to_mono()[1] # 右声道

        # 分别保存左右声道文件
        left_file_path = f"left_channel_{uuid.uuid4().hex}.wav"
        right_file_path = f"right_channel_{uuid.uuid4().hex}.wav"
        left_channel.export(left_file_path, format="wav")
        right_channel.export(right_file_path, format="wav")

        # 使用FunASR模型识别左声道和右声道音频文件
        left_result = asr_model.generate(input=left_file_path)
        right_result = asr_model.generate(input=right_file_path)

        # 完成识别后删除临时音频文件
        os.remove(temp_filename)
        os.remove(left_file_path)
        os.remove(right_file_path)

        # 返回识别结果
        result = {
            "left_channel_text": left_result[0].get('text', ''),
            "right_channel_text": right_result[0].get('text', '')
        }

        return JSONResponse(result)

    except Exception as e:
        # 如果出错，返回错误信息
        return JSONResponse({"error": str(e)})

@app.get("/")
def read_root():
    return {"status": "FunASR Stereo API Server is running"}
```

## 步骤三：启动服务并对外暴露接口

假设你将上述代码保存为：`funasr_api.py`

启动FastAPI服务：

```
uvicorn funasr_api:app --host 0.0.0.0 --port 8000
```

此时服务对外接口地址为：

```
http://服务器IP地址:8000/recognize_stereo/
```

可以通过HTTP POST请求上传文件进行语音识别：

```
curl -X POST "http://localhost:8000/recognize_stereo/" -F "file=@your-stereo-audio-file.wav"
```

### 编写Dockerfile（构建你的镜像）

首先，在本地创建一个目录，例如`funasr-api-service`，然后在里面创建文件 `Dockerfile`，内容如下：

```dockerfile
FROM python:3.8-slim

# 设置工作目录
WORKDIR /app

# 安装系统依赖 (ffmpeg 为处理立体声音频的依赖)
RUN apt-get update && apt-get install -y ffmpeg curl git \
    && rm -rf /var/lib/apt/lists/*

# 安装必要的 Python 库
RUN pip install --upgrade pip
RUN pip install fastapi uvicorn pydub funasr-runtime-sdk-cpu

# 拷贝你的API代码到容器
COPY funasr_api.py /app/funasr_api.py

# 暴露端口
EXPOSE 8000

# 启动FastAPI服务程序
CMD ["uvicorn", "funasr_api:app", "--host", "0.0.0.0", "--port", "8000"]
```

### 二、构建镜像

在`funasr-api-service`目录下执行Docker命令构建镜像：

```
docker build -t funasr-api-image .
```

构建完成后，你会得到名为`funasr-api-image`的Docker镜像。

------

### 三、启动Docker容器并开放端口

启动你的Docker容器，把容器内部端口8000映射到你宿主机器的端口8000（也可自己设定其他端口）：

```
docker run -d --name funasr-api-container -p 8000:8000 funasr-api-image
```

此时，你的Docker中的服务对外已经暴露，现在可通过宿主机IP（服务器IP地址）访问：

```
http://服务器IP:8000/recognize_stereo/
```

### 四、测试服务

例如，你可以测试API是否成功对外暴露：

```
curl -X POST "http://服务器IP:8000/recognize_stereo/" -F "file=@your-stereo-audio-file.wav"
```





### 具体解决方法步骤如下：

## 🚩 第一步：检查磁盘空间情况

在Linux环境，先执行以下命令检查你磁盘占用情况：

bash

折叠保存复制

1

df -h

它会显示磁盘占用状况：

bash

折叠保存复制

1

2

Filesystem      Size  Used Avail Use% Mounted on

/dev/sda1        50G   49G    0G 100% /

上述示例表示根目录磁盘(/) 已满(100%)，你就要释放磁盘空间。

------

## 🚨 第二步：清理磁盘空间（必须执行）

推荐快速安全有效的一些清理动作：

① 清理apt缓存文件（如果是Ubunutu或Debian）：

bash

折叠保存复制

```
sudo apt-get clean
sudo apt-get autoclean
```

② 清理 pip 缓存，释放较多磁盘空间：

```
pip cache purge
```

③ 清理临时垃圾文件：

```
sudo rm -rf /tmp/*
sudo rm -rf /var/tmp/*
```

④ 清理无用的docker镜像或容器（若有docker环境）：

```
docker system prune -a -f
```

⑤ 删除不再使用的系统日志或文件（如老旧的logs或文件）：

```
sudo journalctl --vacuum-size=500M  # 清理日志到500M

sudo rm -rf /var/log/*.gz

```

```
sudo apt-get clean && sudo apt-get autoclean
pip cache purge
sudo journalctl --vacuum-size=500M
sudo rm -rf /tmp/* /var/tmp/* /var/log/*.gz
docker system prune -a -f
df -h
```



/root/autodl-tmp/FunASR
/autodl-tmp/FunASR/python/wav_file

### **1. 确保已安装 GPU 版 PyTorch**

FunASR 依赖 PyTorch（GPU 版），首先验证是否正确安装：

```
python -c "import torch; print(torch.__version__, torch.cuda.is_available())"
```

如果输出 `False`，说明 PyTorch 未启用 GPU，需重新安装：

```
pip install torch==2.1.2 torchaudio==2.1.2 --index-url https://download.pytorch.org/whl/cu118  # CUDA 11.8
```

### **2. 安装 FunASR（GPU 版本）**

#### **方法 1：直接安装（推荐）**

```
pip install "funasr[gpu]" -f https://modelscope.cn/packages/funasr/releases.html
```

这会自动安装 GPU 依赖（如 `onnxruntime-gpu`）。

# 查看所有环境
```
conda env list
```



# 激活你的 FunASR 环境（例如 funasr_env）
```
conda activate funasr_env
```



# 确认当前 Python 环境
```
which python
pip list | grep funasr
```



下载加速

```
source /etc/network_turbo
```



miniconda3安装

```
https://cloud.tencent.com/developer/article/1769751?policyId=1004
```

openweb-ui掉了的话要重装

```
pip install open-webui
```

后台启动

```
nohup open-webui serve --port 6006 > openwebui.log 2>&1 &
```

```
pip install "funasr[gpu]" -f https://modelscope.cn/packages/funasr/releases.html --target=/root/autodl-tmp/databak/data/pip
pip install --target=/root/autodl-tmp/databak/data/pip modelscope
pip install --target=/root/autodl-tmp/databak/data/pip torch torchaudio
```



里面其实可以传url的

![image-20250509194449879](https://lyx-study-note-image.oss-cn-shenzhen.aliyuncs.com/img/image-20250509194449879.png)



api包安装

```
pip install uvicorn fastapi sqlalchemy pydantic_settings pymysql 
```





### Funasr完整部署代码

```python
import logging
import sys
from logging.handlers import TimedRotatingFileHandler
from datetime import datetime
import os
import psutil
import torch
import gc
import numpy as np
from scipy.io import wavfile
import re
import concurrent.futures

# 日志配置，务必放在所有import之前
log_formatter = logging.Formatter(
    "%(asctime)s %(levelname)s %(filename)s:%(lineno)d %(message)s"
)

# 控制台日志
console_handler = logging.StreamHandler(sys.stdout)
console_handler.setFormatter(log_formatter)

# 按天切分的文件日志，保留7天
file_handler = TimedRotatingFileHandler(
    "audio_recognition_api.log",
    when="midnight",           # 每天0点切分
    interval=1,
    backupCount=7,             # 保留最近7天日志
    encoding="utf-8",
    utc=False
)
file_handler.setFormatter(log_formatter)

logging.basicConfig(
    level=logging.INFO,
    handlers=[console_handler, file_handler]
)

logger = logging.getLogger(__name__)

from fastapi import FastAPI, HTTPException
from pydantic import BaseModel
import time
import os
import tempfile
from pathlib import Path
from funasr import AutoModel
from pydub import AudioSegment
import httpx
import asyncio
from concurrent.futures import ThreadPoolExecutor
import sqlite3
import threading
import requests
import pymysql
import uuid
import shutil

app = FastAPI()

TEMP_ROOT = Path("/root/autodl-tmp/FunASR/develop/wav_data")
# TEMP_ROOT = Path("D:/wav_data")
TEMP_ROOT.mkdir(parents=True, exist_ok=True)

# 数据库文件路径
SQLITE_DB_PATH = "tasks.db"

class RecognitionRequest(BaseModel):
    callRecordId: int
    fileUrl: str
    channelCode: str
    applyNo: str
    modelChannel: str
    callbackUrl: str
    doubaoEndpoint: str

def download_audio_from_url_sync(url, call_record_id, save_path=None):
    """同步下载音频文件到本地，文件名唯一"""
    try:
        response = requests.get(url, timeout=10, stream=True)
        response.raise_for_status()
        if save_path is None:
            temp_dir = TEMP_ROOT / f"funasr_temp_{call_record_id}_{uuid.uuid4().hex[:8]}"
            temp_dir.mkdir(exist_ok=True)
            save_path = temp_dir / f"downloaded_audio_{call_record_id}.wav"
        with open(save_path, 'wb') as f:
            for chunk in response.iter_content(chunk_size=8192):
                f.write(chunk)
        return str(save_path), temp_dir
    except Exception as e:
        return None, None

def split_stereo_audio(input_file, left_output_file, right_output_file, sample_rate=16000):
    audio = AudioSegment.from_wav(input_file)
    left = audio.split_to_mono()[0].set_frame_rate(sample_rate)
    right = audio.split_to_mono()[1].set_frame_rate(sample_rate)
    left.export(left_output_file, format="wav")
    right.export(right_output_file, format="wav")

# def ensure_wav_16k1ch(input_file, output_file):
#     """
#     自动将音频转换为16kHz、单声道、WAV格式
#     """
#     audio = AudioSegment.from_file(input_file)
#     audio = audio.set_frame_rate(16000).set_channels(1)
#     audio.export(output_file, format="wav")
#     return output_file

def recognize_audio(asr_model, audio_file):
    results = asr_model.generate(input=audio_file)
    return results

async def async_recognize_audio(asr_model, audio_file):
    loop = asyncio.get_event_loop()
    with ThreadPoolExecutor() as pool:
        result = await loop.run_in_executor(pool, recognize_audio, asr_model, audio_file)
    return result

# def check_audio_valid(audio_path, min_duration_ms=1000):
#     try:
#         audio = AudioSegment.from_wav(audio_path)
#         logger.info(f"音频检查: {audio_path}, 时长: {len(audio)}ms, 采样率: {audio.frame_rate}, 声道: {audio.channels}")
#         if len(audio) < min_duration_ms:
#             raise Exception(f"音频过短: {len(audio)}ms")
#         if audio.frame_rate != 16000:
#             raise Exception(f"采样率不是16k: {audio.frame_rate}")
#         if audio.channels < 1:
#             raise Exception(f"声道数异常: {audio.channels}")
#         return True
#     except Exception as e:
#         logger.error(f"音频有效性检查失败: {audio_path}, error: {e}")
#         return False

def log_resource_usage():
    process = psutil.Process(os.getpid())
    logger.info(f"内存占用: {process.memory_info().rss / 1024 / 1024:.2f} MB, 打开文件数: {process.num_fds()}")

# 在全局只加载一次模型
asr_model = AutoModel(
    model="iic/speech_paraformer-large-vad-punc_asr_nat-zh-cn-16k-common-vocab8404-pytorch",
    vad_model="fsmn-vad",
    punc_model="ct-punc",
    vad_kwargs={"max_single_segment_time": 30000},
    device="cuda:0",
    spk_mode="punc_segment",
    sentence_timestamp=True,
    en_post_proc=True
)

def cleanup_resources():
    gc.collect()
    torch.cuda.empty_cache()

def resource_cleanup_loop(interval=600):
    while True:
        cleanup_resources()
        logger.info("定期资源清理完成")
        time.sleep(interval)

cleanup_thread = threading.Thread(target=resource_cleanup_loop, daemon=True)
cleanup_thread.start()

@app.get("/health")
def health_check():
    mem = psutil.Process().memory_info().rss / 1024 / 1024
    return {"status": "ok", "memory_MB": mem, "threads": threading.active_count()}

@app.post("/recognize-audio")
async def recognize_audio_endpoint(request: RecognitionRequest):
    logger.info(f"收到新任务请求: callRecordId={request.callRecordId}, fileUrl={request.fileUrl}")
    try:
        conn = sqlite3.connect(SQLITE_DB_PATH)
        cursor = conn.cursor()
        cursor.execute('''
            CREATE TABLE IF NOT EXISTS tasks (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                callRecordId INTEGER UNIQUE,
                fileUrl TEXT,
                channelCode TEXT,
                applyNo TEXT,
                modelChannel TEXT,
                callbackUrl TEXT,
                doubaoEndpoint TEXT,
                status TEXT
            )
        ''')
        try:
            cursor.execute('''
                INSERT INTO tasks (callRecordId, fileUrl, channelCode, applyNo, modelChannel, callbackUrl, doubaoEndpoint, status)
                VALUES (?, ?, ?, ?, ?, ?, ?, ?)
            ''', (
                request.callRecordId,
                request.fileUrl,
                request.channelCode,
                request.applyNo,
                request.modelChannel,
                request.callbackUrl,
                request.doubaoEndpoint,
                "pending"
            ))
            conn.commit()
            logger.info(f"任务入库成功: callRecordId={request.callRecordId}")
        except sqlite3.IntegrityError:
            logger.warning(f"任务已存在: callRecordId={request.callRecordId}")
            conn.close()
            return {"message": f"任务已存在，callRecordId={request.callRecordId}"}
        conn.close()
        return {"message": "任务已入库，稍后回调结果"}
    except Exception as e:
        logger.error(f"任务入库失败: callRecordId={request.callRecordId}, error={e}")
        raise HTTPException(status_code=500, detail=f"入库失败: {e}")

@app.get("/process-pending-tasks")
def process_pending_tasks():
    logger.info("开始处理pending任务")
    try:
        conn = sqlite3.connect(SQLITE_DB_PATH)
        cursor = conn.cursor()
        cursor.execute('SELECT id FROM tasks WHERE status = "pending"')
        task_ids = [row[0] for row in cursor.fetchall()]
        logger.info(f"本次待处理任务数: {len(task_ids)}")
        if not task_ids:
            conn.close()
            logger.info("无待处理任务")
            return {"message": "无待处理任务"}
        cursor.executemany('UPDATE tasks SET status = "processing" WHERE id = ?', [(tid,) for tid in task_ids])
        conn.commit()
        cursor.execute(f'SELECT * FROM tasks WHERE id IN ({",".join(["?"]*len(task_ids))})', task_ids)
        tasks = cursor.fetchall()
        processed = 0
        failed = 0

        logger.info("ASR模型加载完成")

        for task in tasks:
            task_id, callRecordId, fileUrl, channelCode, applyNo, modelChannel, callbackUrl, doubaoEndpoint, status = task
            logger.info(f"开始处理任务: id={task_id}, callRecordId={callRecordId}")
            temp_dir = None
            try:
                logger.info("准备下载音频")
                input_file, temp_dir = download_audio_from_url_sync(fileUrl, callRecordId)
                logger.info("音频下载完成")
                if input_file is None:
                    raise Exception("无法下载音频文件")
                logger.info(f"音频下载成功: {input_file}")

                # 直接用下载下来的音频文件作为输入
                standard_wav = input_file  # input_file 就是下载下来的文件路径
                left_audio = temp_dir / f"left_channel_{callRecordId}.wav"
                right_audio = temp_dir / f"right_channel_{callRecordId}.wav"
                logger.info("准备分离音频")
                split_stereo_audio(standard_wav, left_audio, right_audio)
                logger.info("音频分离完成")
                logger.info(f"音频分离成功: left={left_audio}, right={right_audio}")

                logger.info(f"左声道文件: {left_audio}, 右声道文件: {right_audio}")
                # check_audio_valid(left_audio)
                # check_audio_valid(right_audio)

                logger.info("准备左声道识别")
                try:
                    left_result = recognize_audio(asr_model, str(left_audio))
                except Exception as e:
                    logger.error(f"左声道识别失败: {e}")
                    left_result = f"VAD识别失败: {e}"
                    if is_fatal_index_error(e):
                        cursor.execute('UPDATE tasks SET status = "failed" WHERE id = ?', (task_id,))
                        conn.commit()
                        failed += 1
                        cleanup_resources()
                        check_and_restart_if_oom(force_restart=True)
                        continue

                try:
                    right_result = recognize_audio(asr_model, str(right_audio))
                except Exception as e:
                    logger.error(f"右声道识别失败: {e}")
                    right_result = f"VAD识别失败: {e}"
                    if is_fatal_index_error(e):
                        cursor.execute('UPDATE tasks SET status = "failed" WHERE id = ?', (task_id,))
                        conn.commit()
                        failed += 1
                        cleanup_resources()
                        check_and_restart_if_oom(force_restart=True)
                        continue

                logger.info(f"识别完成: left_result={str(left_result)[:100]}, right_result={str(right_result)[:100]}")

                processing_time = datetime.now().strftime("%Y-%m-%d %H:%M:%S")
                callback_data = {
                    "taskId": str(task_id),
                    "callRecordId": callRecordId,
                    "status": "success",
                    "processingTime": processing_time,
                    "leftChannelResult": str(left_result),
                    "rightChannelResult": str(right_result),
                    "errorMessage": "",
                    "channelCode": channelCode,
                    "applyNo": applyNo,
                    "modelChannel": modelChannel,
                    "doubaoEndpoint": doubaoEndpoint
                }

                logger.info(f"回调数据: {callback_data}")

                response = requests.post(callbackUrl, json=callback_data)
                response.raise_for_status()
                logger.info(f"回调成功: {callbackUrl}")

                cursor.execute('UPDATE tasks SET status = "completed" WHERE id = ?', (task_id,))
                conn.commit()
                processed += 1

            except Exception as e:
                cursor.execute('UPDATE tasks SET status = "failed" WHERE id = ?', (task_id,))
                conn.commit()
                logger.error(f"任务处理失败: id={task_id}, callRecordId={callRecordId}, error={e}")
                failed += 1
            finally:
                if temp_dir and temp_dir.exists():
                    shutil.rmtree(temp_dir, ignore_errors=True)
                    logger.info(f"已清理临时目录: {temp_dir}")
                log_resource_usage()
                cleanup_resources()

        conn.close()
        logger.info(f"本次处理完成，成功：{processed}，失败：{failed}")
        return {"message": f"处理完成，成功：{processed}，失败：{failed}"}
    except Exception as e:
        logger.error(f"处理任务时出错: {e}")
        raise HTTPException(status_code=500, detail=f"处理任务时出错: {e}")

def cleanup_resources():
    gc.collect()
    try:
        torch.cuda.empty_cache()
    except Exception:
        pass

def check_and_restart_if_oom(memory_limit_mb=16000, force_restart=False):
    """
    检查内存占用，或收到致命错误时强制重启
    """
    process = psutil.Process(os.getpid())
    mem_mb = process.memory_info().rss / 1024 / 1024
    logger.info(f"当前内存占用: {mem_mb:.2f} MB")
    if force_restart or mem_mb > memory_limit_mb:
        logger.error(f"触发自动重启（force={force_restart}，内存={mem_mb:.2f}MB）！")
        try:
            # 重启前将processing任务重置为pending
            conn = sqlite3.connect(SQLITE_DB_PATH)
            cursor = conn.cursor()
            cursor.execute('UPDATE tasks SET status = "pending" WHERE status = "processing"')
            conn.commit()
            conn.close()
        except Exception as e:
            logger.error(f"重启前重置processing任务失败: {e}")
        try:
            import torch
            torch.cuda.empty_cache()
        except Exception:
            pass
        os.execv(sys.executable, [sys.executable] + sys.argv)

def is_fatal_index_error(e):
    """
    判断是否为 index xxx is out of bounds for dimension 0 with size xxx 且两数相等的致命错误
    或 'list index out of range' 这类致命错误
    """
    msg = str(e)
    # 检查 index out of bounds
    match = re.search(r'index (\d+) is out of bounds for dimension 0 with size (\d+)', msg)
    if match:
        idx, size = int(match.group(1)), int(match.group(2))
        if idx == size:
            return True
    # 检查 list index out of range
    if "list index out of range" in msg:
        return True
    return False

def recognize_audio_with_timeout(asr_model, audio_file, timeout=60):
    with concurrent.futures.ThreadPoolExecutor() as executor:
        future = executor.submit(recognize_audio, asr_model, audio_file)
        return future.result(timeout=timeout)

if __name__ == "__main__":
    import uvicorn
    uvicorn.run(app, host="127.0.0.1", port=2000)
```

### Funasr完整代码（投入生产使用）

**上面的代码比较粗糙，每次都需要加载模型并且速度及慢，还很容易导致OOM所以通过努力重新优化了一版代码，基本上可以3秒~5秒处理一条音频**

```python
import logging
import sys
from logging.handlers import TimedRotatingFileHandler
from datetime import datetime
import os
import psutil
import torch
import gc
import numpy as np
from scipy.io import wavfile
import re
import concurrent.futures

# 日志配置，务必放在所有import之前
log_formatter = logging.Formatter(
    "%(asctime)s %(levelname)s %(filename)s:%(lineno)d %(message)s"
)

# 控制台日志
console_handler = logging.StreamHandler(sys.stdout)
console_handler.setFormatter(log_formatter)

# 按天切分的文件日志，保留7天
file_handler = TimedRotatingFileHandler(
    "audio_recognition_api.log",
    when="midnight",           # 每天0点切分
    interval=1,
    backupCount=7,             # 保留最近7天日志
    encoding="utf-8",
    utc=False
)
file_handler.setFormatter(log_formatter)

logging.basicConfig(
    level=logging.INFO,
    handlers=[console_handler, file_handler]
)

logger = logging.getLogger(__name__)

from fastapi import FastAPI, HTTPException
from pydantic import BaseModel
import time
import os
import tempfile
from pathlib import Path
from funasr import AutoModel
from pydub import AudioSegment
import httpx
import asyncio
from concurrent.futures import ThreadPoolExecutor
import sqlite3
import threading
import requests
import pymysql
import uuid
import shutil

app = FastAPI()

TEMP_ROOT = Path("/root/autodl-tmp/FunASR/develop/wav_data")
# TEMP_ROOT = Path("D:/wav_data")
TEMP_ROOT.mkdir(parents=True, exist_ok=True)
# 数据库文件路径
SQLITE_DB_PATH = "tasks.db"

class RecognitionRequest(BaseModel):
    callRecordId: int
    fileUrl: str
    channelCode: str
    applyNo: str
    modelChannel: str
    callbackUrl: str
    doubaoEndpoint: str

def download_audio_from_url_sync(url, call_record_id, save_path=None):
    """同步下载音频文件到本地，文件名唯一"""
    try:
        response = requests.get(url, timeout=10, stream=True)
        response.raise_for_status()
        if save_path is None:
            temp_dir = TEMP_ROOT / f"funasr_temp_{call_record_id}_{uuid.uuid4().hex[:8]}"
            temp_dir.mkdir(exist_ok=True)
            save_path = temp_dir / f"downloaded_audio_{call_record_id}.wav"
        with open(save_path, 'wb') as f:
            for chunk in response.iter_content(chunk_size=8192):
                f.write(chunk)
        return str(save_path), temp_dir
    except Exception as e:
        return None, None

def split_stereo_audio(input_file, left_output_file, right_output_file, sample_rate=16000):
    audio = AudioSegment.from_wav(input_file)
    left = audio.split_to_mono()[0].set_frame_rate(sample_rate)
    right = audio.split_to_mono()[1].set_frame_rate(sample_rate)
    left.export(left_output_file, format="wav")
    right.export(right_output_file, format="wav")

# def ensure_wav_16k1ch(input_file, output_file):
#     """
#     自动将音频转换为16kHz、单声道、WAV格式
#     """
#     audio = AudioSegment.from_file(input_file)
#     audio = audio.set_frame_rate(16000).set_channels(1)
#     audio.export(output_file, format="wav")
#     return output_file

def recognize_audio(asr_model, audio_file):
    results = asr_model.generate(input=audio_file)
    return results

async def async_recognize_audio(asr_model, audio_file):
    loop = asyncio.get_event_loop()
    with ThreadPoolExecutor() as pool:
        result = await loop.run_in_executor(pool, recognize_audio, asr_model, audio_file)
    return result

# def check_audio_valid(audio_path, min_duration_ms=1000):
#     try:
#         audio = AudioSegment.from_wav(audio_path)
#         logger.info(f"音频检查: {audio_path}, 时长: {len(audio)}ms, 采样率: {audio.frame_rate}, 声道: {audio.channels}")
#         if len(audio) < min_duration_ms:
#             raise Exception(f"音频过短: {len(audio)}ms")
#         if audio.frame_rate != 16000:
#             raise Exception(f"采样率不是16k: {audio.frame_rate}")
#         if audio.channels < 1:
#             raise Exception(f"声道数异常: {audio.channels}")
#         return True
#     except Exception as e:
#         logger.error(f"音频有效性检查失败: {audio_path}, error: {e}")
#         return False

def log_resource_usage():
    process = psutil.Process(os.getpid())
    logger.info(f"内存占用: {process.memory_info().rss / 1024 / 1024:.2f} MB, 打开文件数: {process.num_fds()}")

# 在全局只加载一次模型
asr_model = AutoModel(
    model="iic/speech_paraformer-large-vad-punc_asr_nat-zh-cn-16k-common-vocab8404-pytorch",
    vad_model="fsmn-vad",
    punc_model="ct-punc",
    vad_kwargs={"max_single_segment_time": 30000},
    device="cuda:0",
    spk_mode="punc_segment",
    sentence_timestamp=True,
    en_post_proc=True
)

def cleanup_resources():
    gc.collect()
    torch.cuda.empty_cache()

def resource_cleanup_loop(interval=300):
    while True:
        cleanup_resources()
        logger.info("定期资源清理完成")
        time.sleep(interval)

cleanup_thread = threading.Thread(target=resource_cleanup_loop, daemon=True)
cleanup_thread.start()

@app.get("/health")
def health_check():
    mem = psutil.Process().memory_info().rss / 1024 / 1024
    return {"status": "ok", "memory_MB": mem, "threads": threading.active_count()}

@app.post("/recognize-audio")
async def recognize_audio_endpoint(request: RecognitionRequest):
    logger.info(f"收到新任务请求: callRecordId={request.callRecordId}, fileUrl={request.fileUrl}")
    try:
        conn = sqlite3.connect(SQLITE_DB_PATH)
        cursor = conn.cursor()
        cursor.execute('''
            CREATE TABLE IF NOT EXISTS tasks (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                callRecordId INTEGER UNIQUE,
                fileUrl TEXT,
                channelCode TEXT,
                applyNo TEXT,
                modelChannel TEXT,
                callbackUrl TEXT,
                doubaoEndpoint TEXT,
                status TEXT
            )
        ''')
        try:
            cursor.execute('''
                INSERT INTO tasks (callRecordId, fileUrl, channelCode, applyNo, modelChannel, callbackUrl, doubaoEndpoint, status)
                VALUES (?, ?, ?, ?, ?, ?, ?, ?)
            ''', (
                request.callRecordId,
                request.fileUrl,
                request.channelCode,
                request.applyNo,
                request.modelChannel,
                request.callbackUrl,
                request.doubaoEndpoint,
                "pending"
            ))
            conn.commit()
            logger.info(f"任务入库成功: callRecordId={request.callRecordId}")
        except sqlite3.IntegrityError:
            logger.warning(f"任务已存在: callRecordId={request.callRecordId}")
            conn.close()
            return {"message": f"任务已存在，callRecordId={request.callRecordId}"}
        conn.close()
        return {"message": "任务已入库，稍后回调结果"}
    except Exception as e:
        logger.error(f"任务入库失败: callRecordId={request.callRecordId}, error={e}")
        raise HTTPException(status_code=500, detail=f"入库失败: {e}")

@app.get("/process-pending-tasks")
def process_pending_tasks():
    logger.info("开始处理pending任务")
    try:
        conn = sqlite3.connect(SQLITE_DB_PATH)
        cursor = conn.cursor()
        cursor.execute('SELECT id FROM tasks WHERE status = "pending"')
        task_ids = [row[0] for row in cursor.fetchall()]
        logger.info(f"本次待处理任务数: {len(task_ids)}")
        if not task_ids:
            conn.close()
            logger.info("无待处理任务")
            return {"message": "无待处理任务"}
        cursor.executemany('UPDATE tasks SET status = "processing" WHERE id = ?', [(tid,) for tid in task_ids])
        conn.commit()
        cursor.execute(f'SELECT * FROM tasks WHERE id IN ({",".join(["?"]*len(task_ids))})', task_ids)
        tasks = cursor.fetchall()
        processed = 0
        failed = 0

        logger.info("ASR模型加载完成")

        for task in tasks:
            task_id, callRecordId, fileUrl, channelCode, applyNo, modelChannel, callbackUrl, doubaoEndpoint, status = task
            logger.info(f"开始处理任务: id={task_id}, callRecordId={callRecordId}")
            temp_dir = None
            try:
                logger.info("准备下载音频")
                input_file, temp_dir = download_audio_from_url_sync(fileUrl, callRecordId)
                logger.info("音频下载完成")
                if input_file is None:
                    raise Exception("无法下载音频文件")
                logger.info(f"音频下载成功: {input_file}")

                # 直接用下载下来的音频文件作为输入
                standard_wav = input_file  # input_file 就是下载下来的文件路径
                left_audio = temp_dir / f"left_channel_{callRecordId}.wav"
                right_audio = temp_dir / f"right_channel_{callRecordId}.wav"
                logger.info("准备分离音频")
                split_stereo_audio(standard_wav, left_audio, right_audio)
                logger.info("音频分离完成")
                logger.info(f"音频分离成功: left={left_audio}, right={right_audio}")

                logger.info(f"左声道文件: {left_audio}, 右声道文件: {right_audio}")
                # check_audio_valid(left_audio)
                # check_audio_valid(right_audio)

                logger.info("准备左声道识别")
                try:
                    left_result = recognize_audio(asr_model, str(left_audio))
                except Exception as e:
                    logger.error(f"左声道识别失败: {e}")
                    left_result = f"VAD识别失败: {e}"
                    if is_fatal_index_error(e):
                        cursor.execute('UPDATE tasks SET status = "failed" WHERE id = ?', (task_id,))
                        conn.commit()
                        failed += 1
                        cleanup_resources()
                        check_and_restart_if_oom(force_restart=True)
                        continue

                try:
                    right_result = recognize_audio(asr_model, str(right_audio))
                except Exception as e:
                    logger.error(f"右声道识别失败: {e}")
                    right_result = f"VAD识别失败: {e}"
                    if is_fatal_index_error(e):
                        cursor.execute('UPDATE tasks SET status = "failed" WHERE id = ?', (task_id,))
                        conn.commit()
                        failed += 1
                        cleanup_resources()
                        check_and_restart_if_oom(force_restart=True)
                        continue

                logger.info(f"识别完成: left_result={str(left_result)[:100]}, right_result={str(right_result)[:100]}")
                processing_time = datetime.now().strftime("%Y-%m-%d %H:%M:%S")
                callback_data = {
                    "taskId": str(task_id),
                    "callRecordId": callRecordId,
                    "status": "success",
                    "processingTime": processing_time,
                    "leftChannelResult": str(left_result),
                    "rightChannelResult": str(right_result),
                    "errorMessage": "",
                    "channelCode": channelCode,
                    "applyNo": applyNo,
                    "modelChannel": modelChannel,
                    "doubaoEndpoint": doubaoEndpoint
                }

                logger.info(f"回调数据: {callback_data}")

                response = requests.post(callbackUrl, json=callback_data)
                response.raise_for_status()
                logger.info(f"回调成功: {callbackUrl}")

                cursor.execute('UPDATE tasks SET status = "completed" WHERE id = ?', (task_id,))
                conn.commit()
                processed += 1

            except Exception as e:
                cursor.execute('UPDATE tasks SET status = "failed" WHERE id = ?', (task_id,))
                conn.commit()
                logger.error(f"任务处理失败: id={task_id}, callRecordId={callRecordId}, error={e}")
                failed += 1
            finally:
                if temp_dir and temp_dir.exists():
                    shutil.rmtree(temp_dir, ignore_errors=True)
                    logger.info(f"已清理临时目录: {temp_dir}")
                log_resource_usage()
                cleanup_resources()

        conn.close()
        logger.info(f"本次处理完成，成功：{processed}，失败：{failed}")
        return {"message": f"处理完成，成功：{processed}，失败：{failed}"}
    except Exception as e:
        logger.error(f"处理任务时出错: {e}")
        raise HTTPException(status_code=500, detail=f"处理任务时出错: {e}")

def cleanup_resources():
    gc.collect()
    try:
        torch.cuda.empty_cache()
    except Exception:
        pass

def check_and_restart_if_oom(memory_limit_mb=16000, force_restart=False):
    """
    检查内存占用，或收到致命错误时强制重启
    """
    process = psutil.Process(os.getpid())
    mem_mb = process.memory_info().rss / 1024 / 1024
    logger.info(f"当前内存占用: {mem_mb:.2f} MB")
    if force_restart or mem_mb > memory_limit_mb:
        logger.error(f"触发自动重启（force={force_restart}，内存={mem_mb:.2f}MB）！")
        try:
            # 重启前将processing任务重置为pending
            conn = sqlite3.connect(SQLITE_DB_PATH)
            cursor = conn.cursor()
            cursor.execute('UPDATE tasks SET status = "pending" WHERE status = "processing"')
            conn.commit()
            conn.close()
        except Exception as e:
            logger.error(f"重启前重置processing任务失败: {e}")
        try:
            import torch
            torch.cuda.empty_cache()
        except Exception:
            pass
        os.execv(sys.executable, [sys.executable] + sys.argv)

def is_fatal_index_error(e):
    """
    判断是否为 index xxx is out of bounds for dimension 0 with size xxx 且两数相等的致命错误
    或 'list index out of range' 这类致命错误
    """
    msg = str(e)
    # 检查 index out of bounds
    match = re.search(r'index (\d+) is out of bounds for dimension 0 with size (\d+)', msg)
    if match:
        idx, size = int(match.group(1)), int(match.group(2))
        if idx == size:
            return True
    # 检查 list index out of range
    if "list index out of range" in msg:
        return True
    return False

def recognize_audio_with_timeout(asr_model, audio_file, timeout=60):
    with concurrent.futures.ThreadPoolExecutor() as executor:
        future = executor.submit(recognize_audio, asr_model, audio_file)
        return future.result(timeout=timeout)
if __name__ == "__main__":
    import uvicorn
    uvicorn.run(app, host="127.0.0.1", port=2000)
```



### Funasr多节点处理任务代码

**当前如果并发量太高的还是有点顶不住，所以需要再开一个节点处理任务才处理得过来**

主任务

```python
import logging
import sys
from logging.handlers import TimedRotatingFileHandler
from datetime import datetime
import os
import psutil
import torch
import gc
import numpy as np
from scipy.io import wavfile
import re
import concurrent.futures

# 日志配置，务必放在所有import之前
log_formatter = logging.Formatter(
    "%(asctime)s %(levelname)s %(filename)s:%(lineno)d %(message)s"
)

# 控制台日志
console_handler = logging.StreamHandler(sys.stdout)
console_handler.setFormatter(log_formatter)

# 按天切分的文件日志，保留7天
file_handler = TimedRotatingFileHandler(
    "audio_recognition_api.log",
    when="midnight",           # 每天0点切分
    interval=1,
    backupCount=7,             # 保留最近7天日志
    encoding="utf-8",
    utc=False
)
file_handler.setFormatter(log_formatter)

logging.basicConfig(
    level=logging.INFO,
    handlers=[console_handler, file_handler]
)

logger = logging.getLogger(__name__)

from fastapi import FastAPI, HTTPException, Request
from pydantic import BaseModel
import time
import os
import tempfile
from pathlib import Path
from funasr import AutoModel
from pydub import AudioSegment
import httpx
import asyncio
from concurrent.futures import ThreadPoolExecutor
import sqlite3
import threading
import requests
import pymysql
import uuid
import shutil

app = FastAPI()

TEMP_ROOT = Path("/root/autodl-tmp/FunASR/develop/wav_data")
# TEMP_ROOT = Path("D:/wav_data")
TEMP_ROOT.mkdir(parents=True, exist_ok=True)

# 数据库文件路径
SQLITE_DB_PATH = "tasks.db"

class RecognitionRequest(BaseModel):
    callRecordId: int
    fileUrl: str
    channelCode: str
    applyNo: str
    modelChannel: str
    callbackUrl: str
    doubaoEndpoint: str

def download_audio_from_url_sync(url, call_record_id, save_path=None):
    """同步下载音频文件到本地，文件名唯一"""
    try:
        response = requests.get(url, timeout=10, stream=True)
        response.raise_for_status()
        if save_path is None:
            temp_dir = TEMP_ROOT / f"funasr_temp_{call_record_id}_{uuid.uuid4().hex[:8]}"
            temp_dir.mkdir(exist_ok=True)
            save_path = temp_dir / f"downloaded_audio_{call_record_id}.wav"
        with open(save_path, 'wb') as f:
            for chunk in response.iter_content(chunk_size=8192):
                f.write(chunk)
        return str(save_path), temp_dir
    except Exception as e:
        return None, None

def split_stereo_audio(input_file, left_output_file, right_output_file, sample_rate=16000):
    audio = AudioSegment.from_wav(input_file)
    left = audio.split_to_mono()[0].set_frame_rate(sample_rate)
    right = audio.split_to_mono()[1].set_frame_rate(sample_rate)
    left.export(left_output_file, format="wav")
    right.export(right_output_file, format="wav")

# def ensure_wav_16k1ch(input_file, output_file):
#     """
#     自动将音频转换为16kHz、单声道、WAV格式
#     """
#     audio = AudioSegment.from_file(input_file)
#     audio = audio.set_frame_rate(16000).set_channels(1)
#     audio.export(output_file, format="wav")
#     return output_file

def recognize_audio(asr_model, audio_file):
    results = asr_model.generate(input=audio_file)
    return results

async def async_recognize_audio(asr_model, audio_file):
    loop = asyncio.get_event_loop()
    with ThreadPoolExecutor() as pool:
        result = await loop.run_in_executor(pool, recognize_audio, asr_model, audio_file)
    return result

# def check_audio_valid(audio_path, min_duration_ms=1000):
#     try:
#         audio = AudioSegment.from_wav(audio_path)
#         logger.info(f"音频检查: {audio_path}, 时长: {len(audio)}ms, 采样率: {audio.frame_rate}, 声道: {audio.channels}")
#         if len(audio) < min_duration_ms:
#             raise Exception(f"音频过短: {len(audio)}ms")
#         if audio.frame_rate != 16000:
#             raise Exception(f"采样率不是16k: {audio.frame_rate}")
#         if audio.channels < 1:
#             raise Exception(f"声道数异常: {audio.channels}")
#         return True
#     except Exception as e:
#         logger.error(f"音频有效性检查失败: {audio_path}, error: {e}")
#         return False

def log_resource_usage():
    process = psutil.Process(os.getpid())
    logger.info(f"内存占用: {process.memory_info().rss / 1024 / 1024:.2f} MB, 打开文件数: {process.num_fds()}")

# 在全局只加载一次模型
asr_model = AutoModel(
    model="iic/speech_paraformer-large-vad-punc_asr_nat-zh-cn-16k-common-vocab8404-pytorch",
    vad_model="fsmn-vad",
    punc_model="ct-punc",
    vad_kwargs={"max_single_segment_time": 30000},
    device="cuda:0",
    spk_mode="punc_segment",
    sentence_timestamp=True,
    en_post_proc=True
)

def cleanup_resources():
    gc.collect()
    torch.cuda.empty_cache()

def resource_cleanup_loop(interval=600):
    while True:
        cleanup_resources()
        logger.info("定期资源清理完成")
        time.sleep(interval)

cleanup_thread = threading.Thread(target=resource_cleanup_loop, daemon=True)
cleanup_thread.start()

@app.get("/health")
def health_check():
    mem = psutil.Process().memory_info().rss / 1024 / 1024
    return {"status": "ok", "memory_MB": mem, "threads": threading.active_count()}

@app.post("/recognize-audio")
async def recognize_audio_endpoint(request: RecognitionRequest):
    logger.info(f"收到新任务请求: callRecordId={request.callRecordId}, fileUrl={request.fileUrl}")
    try:
        conn = sqlite3.connect(SQLITE_DB_PATH)
        cursor = conn.cursor()
        cursor.execute('''
            CREATE TABLE IF NOT EXISTS tasks (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                callRecordId INTEGER UNIQUE,
                fileUrl TEXT,
                channelCode TEXT,
                applyNo TEXT,
                modelChannel TEXT,
                callbackUrl TEXT,
                doubaoEndpoint TEXT,
                status TEXT
            )
        ''')
        try:
            cursor.execute('''
                INSERT INTO tasks (callRecordId, fileUrl, channelCode, applyNo, modelChannel, callbackUrl, doubaoEndpoint, status)
                VALUES (?, ?, ?, ?, ?, ?, ?, ?)
            ''', (
                request.callRecordId,
                request.fileUrl,
                request.channelCode,
                request.applyNo,
                request.modelChannel,
                request.callbackUrl,
                request.doubaoEndpoint,
                "pending"
            ))
            conn.commit()
            logger.info(f"任务入库成功: callRecordId={request.callRecordId}")
        except sqlite3.IntegrityError:
            logger.warning(f"任务已存在: callRecordId={request.callRecordId}")
            conn.close()
            return {"message": f"任务已存在，callRecordId={request.callRecordId}"}
        conn.close()
        return {"message": "任务已入库，稍后回调结果"}
    except Exception as e:
        logger.error(f"任务入库失败: callRecordId={request.callRecordId}, error={e}")
        raise HTTPException(status_code=500, detail=f"入库失败: {e}")

@app.get("/process-pending-tasks")
def process_pending_tasks():
    logger.info("开始处理pending任务")
    try:
        conn = sqlite3.connect(SQLITE_DB_PATH)
        cursor = conn.cursor()
        cursor.execute('SELECT id FROM tasks WHERE status = "pending"')
        task_ids = [row[0] for row in cursor.fetchall()]
        logger.info(f"本次待处理任务数: {len(task_ids)}")
        if not task_ids:
            conn.close()
            logger.info("无待处理任务")
            return {"message": "无待处理任务"}
        # 均匀分配任务
        half = len(task_ids) // 2
        local_ids = task_ids[:half]
        worker_ids = task_ids[half:]
        processed = 0
        failed = 0
        # 本地处理部分
        if local_ids:
            cursor.executemany('UPDATE tasks SET status = "processing" WHERE id = ?', [(tid,) for tid in local_ids])
            conn.commit()
            cursor.execute(f'SELECT * FROM tasks WHERE id IN ({",".join(["?"]*len(local_ids))})', local_ids)
            tasks = cursor.fetchall()
            logger.info(f"本地处理任务数: {len(tasks)}")
            for task in tasks:
                task_id, callRecordId, fileUrl, channelCode, applyNo, modelChannel, callbackUrl, doubaoEndpoint, status = task
                logger.info(f"开始本地处理任务: id={task_id}, callRecordId={callRecordId}")
                temp_dir = None
                try:
                    input_file, temp_dir = download_audio_from_url_sync(fileUrl, callRecordId)
                    if input_file is None:
                        raise Exception("无法下载音频文件")
                    standard_wav = input_file
                    left_audio = temp_dir / f"left_channel_{callRecordId}.wav"
                    right_audio = temp_dir / f"right_channel_{callRecordId}.wav"
                    split_stereo_audio(standard_wav, left_audio, right_audio)
                    try:
                        left_result = recognize_audio(asr_model, str(left_audio))
                    except Exception as e:
                        logger.error(f"左声道识别失败: {e}")
                        left_result = f"VAD识别失败: {e}"
                        if is_fatal_index_error(e):
                            cursor.execute('UPDATE tasks SET status = "failed" WHERE id = ?', (task_id,))
                            conn.commit()
                            failed += 1
                            cleanup_resources()
                            check_and_restart_if_oom(force_restart=True)
                            continue
                    try:
                        right_result = recognize_audio(asr_model, str(right_audio))
                    except Exception as e:
                        logger.error(f"右声道识别失败: {e}")
                        right_result = f"VAD识别失败: {e}"
                        if is_fatal_index_error(e):
                            cursor.execute('UPDATE tasks SET status = "failed" WHERE id = ?', (task_id,))
                            conn.commit()
                            failed += 1
                            cleanup_resources()
                            check_and_restart_if_oom(force_restart=True)
                            continue
                    processing_time = datetime.now().strftime("%Y-%m-%d %H:%M:%S")
                    callback_data = {
                        "taskId": str(task_id),
                        "callRecordId": callRecordId,
                        "status": "success",
                        "processingTime": processing_time,
                        "leftChannelResult": str(left_result),
                        "rightChannelResult": str(right_result),
                        "errorMessage": "",
                        "channelCode": channelCode,
                        "applyNo": applyNo,
                        "modelChannel": modelChannel,
                        "doubaoEndpoint": doubaoEndpoint
                    }
                    response = requests.post(callbackUrl, json=callback_data)
                    response.raise_for_status()
                    cursor.execute('UPDATE tasks SET status = "completed" WHERE id = ?', (task_id,))
                    conn.commit()
                    processed += 1
                except Exception as e:
                    cursor.execute('UPDATE tasks SET status = "failed" WHERE id = ?', (task_id,))
                    conn.commit()
                    logger.error(f"任务处理失败: id={task_id}, callRecordId={callRecordId}, error={e}")
                    failed += 1
                finally:
                    if temp_dir and temp_dir.exists():
                        shutil.rmtree(temp_dir, ignore_errors=True)
                        logger.info(f"已清理临时目录: {temp_dir}")
                    log_resource_usage()
                    cleanup_resources()
        # 分配给worker部分
        worker_result = None
        if worker_ids:
            try:
                # 这里假设worker服务运行在127.0.0.1:2100，且支持POST /process-pending-tasks，参数为json: {"task_ids": [id1, id2, ...]}
                worker_url = "http://127.0.0.1:2100/process-pending-tasks"
                worker_result = requests.post(worker_url, json={"task_ids": worker_ids}, timeout=600).json()
                logger.info(f"worker处理结果: {worker_result}")
            except Exception as e:
                logger.error(f"worker调用失败: {e}")
                # worker失败后将这些任务重置为pending
                try:
                    cursor.executemany('UPDATE tasks SET status = \"pending\" WHERE id = ?', [(tid,) for tid in worker_ids])
                    conn.commit()
                    logger.info(f"worker失败，已重置{len(worker_ids)}个任务为pending")
                except Exception as db_e:
                    logger.error(f"重置worker任务为pending失败: {db_e}")
        conn.close()
        return {"本地处理": f"成功：{processed}，失败：{failed}", "worker处理": worker_result}
    except Exception as e:
        logger.error(f"处理任务时出错: {e}")
        raise HTTPException(status_code=500, detail=f"处理任务时出错: {e}")

def cleanup_resources():
    gc.collect()
    try:
        torch.cuda.empty_cache()
    except Exception:
        pass

def check_and_restart_if_oom(memory_limit_mb=16000, force_restart=False):
    """
    检查内存占用，或收到致命错误时强制重启
    """
    process = psutil.Process(os.getpid())
    mem_mb = process.memory_info().rss / 1024 / 1024
    logger.info(f"当前内存占用: {mem_mb:.2f} MB")
    if force_restart or mem_mb > memory_limit_mb:
        logger.error(f"触发自动重启（force={force_restart}，内存={mem_mb:.2f}MB）！")
        try:
            # 重启前将processing任务重置为pending
            conn = sqlite3.connect(SQLITE_DB_PATH)
            cursor = conn.cursor()
            cursor.execute('UPDATE tasks SET status = "pending" WHERE status = "processing"')
            conn.commit()
            conn.close()
        except Exception as e:
            logger.error(f"重启前重置processing任务失败: {e}")
        try:
            import torch
            torch.cuda.empty_cache()
        except Exception:
            pass
        os.execv(sys.executable, [sys.executable] + sys.argv)

def is_fatal_index_error(e):
    """
    判断是否为 index xxx is out of bounds for dimension 0 with size xxx 且两数相等的致命错误
    或 'list index out of range' 这类致命错误
    """
    msg = str(e)
    # 检查 index out of bounds
    match = re.search(r'index (\d+) is out of bounds for dimension 0 with size (\d+)', msg)
    if match:
        idx, size = int(match.group(1)), int(match.group(2))
        if idx == size:
            return True
    # 检查 list index out of range
    if "list index out of range" in msg:
        return True
    return False

def recognize_audio_with_timeout(asr_model, audio_file, timeout=60):
    with concurrent.futures.ThreadPoolExecutor() as executor:
        future = executor.submit(recognize_audio, asr_model, audio_file)
        return future.result(timeout=timeout)

if __name__ == "__main__":
    import uvicorn
    uvicorn.run(app, host="127.0.0.1", port=2000)
```

worker任务节点

```python
import logging
import sys
from logging.handlers import TimedRotatingFileHandler
from datetime import datetime
import os
import psutil
import torch
import gc
import numpy as np
from scipy.io import wavfile
import re
import concurrent.futures
from fastapi import FastAPI, HTTPException, Request, Body
from pydantic import BaseModel
import time
import tempfile
from pathlib import Path
from funasr import AutoModel
from pydub import AudioSegment
import httpx
import asyncio
from concurrent.futures import ThreadPoolExecutor
import sqlite3
import threading
import requests
import pymysql
import uuid
import shutil

# 日志配置
log_formatter = logging.Formatter(
    "%(asctime)s %(levelname)s %(filename)s:%(lineno)d %(message)s"
)
console_handler = logging.StreamHandler(sys.stdout)
console_handler.setFormatter(log_formatter)
file_handler = TimedRotatingFileHandler(
    "worker_funasr.log",
    when="midnight",
    interval=1,
    backupCount=7,
    encoding="utf-8",
    utc=False
)
file_handler.setFormatter(log_formatter)
logging.basicConfig(
    level=logging.INFO,
    handlers=[console_handler, file_handler]
)
logger = logging.getLogger(__name__)

app = FastAPI()

TEMP_ROOT = Path("/root/autodl-tmp/FunASR/develop/wav_data")
TEMP_ROOT.mkdir(parents=True, exist_ok=True)
SQLITE_DB_PATH = "tasks.db"

class RecognitionRequest(BaseModel):
    callRecordId: int
    fileUrl: str
    channelCode: str
    applyNo: str
    modelChannel: str
    callbackUrl: str
    doubaoEndpoint: str

def download_audio_from_url_sync(url, call_record_id, save_path=None):
    try:
        response = requests.get(url, timeout=10, stream=True)
        response.raise_for_status()
        if save_path is None:
            temp_dir = TEMP_ROOT / f"funasr_temp_{call_record_id}_{uuid.uuid4().hex[:8]}"
            temp_dir.mkdir(exist_ok=True)
            save_path = temp_dir / f"downloaded_audio_{call_record_id}.wav"
        with open(save_path, 'wb') as f:
            for chunk in response.iter_content(chunk_size=8192):
                f.write(chunk)
        return str(save_path), temp_dir
    except Exception as e:
        return None, None

def split_stereo_audio(input_file, left_output_file, right_output_file, sample_rate=16000):
    audio = AudioSegment.from_wav(input_file)
    left = audio.split_to_mono()[0].set_frame_rate(sample_rate)
    right = audio.split_to_mono()[1].set_frame_rate(sample_rate)
    left.export(left_output_file, format="wav")
    right.export(right_output_file, format="wav")

def recognize_audio(asr_model, audio_file):
    results = asr_model.generate(input=audio_file)
    return results

def log_resource_usage():
    process = psutil.Process(os.getpid())
    logger.info(f"内存占用: {process.memory_info().rss / 1024 / 1024:.2f} MB, 打开文件数: {process.num_fds()}")

def cleanup_resources():
    gc.collect()
    try:
        torch.cuda.empty_cache()
    except Exception:
        pass

def is_fatal_index_error(e):
    msg = str(e)
    match = re.search(r'index (\d+) is out of bounds for dimension 0 with size (\d+)', msg)
    if match:
        idx, size = int(match.group(1)), int(match.group(2))
        if idx == size:
            return True
    if "list index out of range" in msg:
        return True
    return False

def check_and_restart_if_oom(memory_limit_mb=16000, force_restart=False):
    process = psutil.Process(os.getpid())
    mem_mb = process.memory_info().rss / 1024 / 1024
    logger.info(f"当前内存占用: {mem_mb:.2f} MB")
    if force_restart or mem_mb > memory_limit_mb:
        logger.error(f"触发自动重启（force={force_restart}，内存={mem_mb:.2f}MB）！")
        try:
            conn = sqlite3.connect(SQLITE_DB_PATH)
            cursor = conn.cursor()
            cursor.execute('UPDATE tasks SET status = "pending" WHERE status = "processing"')
            conn.commit()
            conn.close()
        except Exception as e:
            logger.error(f"重启前重置processing任务失败: {e}")
        try:
            import torch
            torch.cuda.empty_cache()
        except Exception:
            pass
        os.execv(sys.executable, [sys.executable] + sys.argv)

# 全局只加载一次模型
asr_model = AutoModel(
    model="iic/speech_paraformer-large-vad-punc_asr_nat-zh-cn-16k-common-vocab8404-pytorch",
    vad_model="fsmn-vad",
    punc_model="ct-punc",
    vad_kwargs={"max_single_segment_time": 30000},
    device="cuda:0",
    spk_mode="punc_segment",
    sentence_timestamp=True,
    en_post_proc=True
)

@app.get("/health")
def health_check():
    mem = psutil.Process().memory_info().rss / 1024 / 1024
    return {"status": "ok", "memory_MB": mem, "threads": threading.active_count()}

@app.post("/process-pending-tasks")
def process_pending_tasks(request: Request, body: dict = Body(...)):
    logger.info("[worker] 开始处理pending任务（指定id）")
    try:
        task_ids = body.get("task_ids", [])
        if not task_ids:
            logger.info("[worker] 未指定任务id，直接返回")
            return {"message": "未指定任务id"}
        conn = sqlite3.connect(SQLITE_DB_PATH)
        cursor = conn.cursor()
        cursor.executemany('UPDATE tasks SET status = "processing" WHERE id = ?', [(tid,) for tid in task_ids])
        conn.commit()
        cursor.execute(f'SELECT * FROM tasks WHERE id IN ({",".join(["?"]*len(task_ids))})', task_ids)
        tasks = cursor.fetchall()
        processed = 0
        failed = 0
        logger.info(f"[worker] 本次待处理任务数: {len(tasks)}")
        for task in tasks:
            task_id, callRecordId, fileUrl, channelCode, applyNo, modelChannel, callbackUrl, doubaoEndpoint, status = task
            logger.info(f"[worker] 开始处理任务: id={task_id}, callRecordId={callRecordId}")
            temp_dir = None
            try:
                input_file, temp_dir = download_audio_from_url_sync(fileUrl, callRecordId)
                if input_file is None:
                    raise Exception("无法下载音频文件")
                standard_wav = input_file
                left_audio = temp_dir / f"left_channel_{callRecordId}.wav"
                right_audio = temp_dir / f"right_channel_{callRecordId}.wav"
                split_stereo_audio(standard_wav, left_audio, right_audio)
                try:
                    left_result = recognize_audio(asr_model, str(left_audio))
                except Exception as e:
                    logger.error(f"[worker] 左声道识别失败: {e}")
                    left_result = f"VAD识别失败: {e}"
                    if is_fatal_index_error(e):
                        cursor.execute('UPDATE tasks SET status = "failed" WHERE id = ?', (task_id,))
                        conn.commit()
                        failed += 1
                        cleanup_resources()
                        check_and_restart_if_oom(force_restart=True)
                        continue
                try:
                    right_result = recognize_audio(asr_model, str(right_audio))
                except Exception as e:
                    logger.error(f"[worker] 右声道识别失败: {e}")
                    right_result = f"VAD识别失败: {e}"
                    if is_fatal_index_error(e):
                        cursor.execute('UPDATE tasks SET status = "failed" WHERE id = ?', (task_id,))
                        conn.commit()
                        failed += 1
                        cleanup_resources()
                        check_and_restart_if_oom(force_restart=True)
                        continue
                processing_time = datetime.now().strftime("%Y-%m-%d %H:%M:%S")
                callback_data = {
                    "taskId": str(task_id),
                    "callRecordId": callRecordId,
                    "status": "success",
                    "processingTime": processing_time,
                    "leftChannelResult": str(left_result),
                    "rightChannelResult": str(right_result),
                    "errorMessage": "",
                    "channelCode": channelCode,
                    "applyNo": applyNo,
                    "modelChannel": modelChannel,
                    "doubaoEndpoint": doubaoEndpoint
                }
                response = requests.post(callbackUrl, json=callback_data)
                response.raise_for_status()
                cursor.execute('UPDATE tasks SET status = "completed" WHERE id = ?', (task_id,))
                conn.commit()
                processed += 1
            except Exception as e:
                cursor.execute('UPDATE tasks SET status = "failed" WHERE id = ?', (task_id,))
                conn.commit()
                logger.error(f"[worker] 任务处理失败: id={task_id}, callRecordId={callRecordId}, error={e}")
                failed += 1
            finally:
                if temp_dir and temp_dir.exists():
                    shutil.rmtree(temp_dir, ignore_errors=True)
                    logger.info(f"[worker] 已清理临时目录: {temp_dir}")
                log_resource_usage()
                cleanup_resources()
        conn.close()
        logger.info(f"[worker] 本次处理完成，成功：{processed}，失败：{failed}")
        return {"message": f"处理完成，成功：{processed}，失败：{failed}"}
    except Exception as e:
        logger.error(f"[worker] 处理任务时出错: {e}")
        raise HTTPException(status_code=500, detail=f"处理任务时出错: {e}")

if __name__ == "__main__":
    import uvicorn
    uvicorn.run(app, host="127.0.0.1", port=2100) 
```



















