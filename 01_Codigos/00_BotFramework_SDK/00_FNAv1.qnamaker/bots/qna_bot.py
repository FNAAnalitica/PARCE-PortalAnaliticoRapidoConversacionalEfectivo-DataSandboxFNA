# Copyright (c) Microsoft Corporation. All rights reserved.
# Licensed under the MIT License.

import time
from datetime import datetime
from botbuilder.ai.qna import QnAMaker, QnAMakerEndpoint
from botbuilder.core import ActivityHandler, ConversationState, UserState, MessageFactory, TurnContext, StoreItem, MemoryStorage
from botbuilder.azure import CosmosDbPartitionedStorage, CosmosDbPartitionedConfig
from botbuilder.schema import ChannelAccount
import string
import random

from config import DefaultConfig
CONFIG = DefaultConfig()

"""
# Creación dirección IP
import socket
## getting the hostname by socket.gethostname() method
hostname = socket.gethostname()
## getting the IP address using socket.gethostbyname() method
ip_address = socket.gethostbyname(hostname)
"""

# Creación abecedario para Id's
all_chars = string.ascii_letters + string.digits


# Función para métricas de fecha y hora
def datetime_from_utc_to_local(utc_datetime):
        now_timestamp = time.time()
        offset = datetime.fromtimestamp(now_timestamp) - datetime.utcfromtimestamp(
            now_timestamp
        )
        result = utc_datetime + offset
        return result.strftime("%d-%m-%y")
    
# Función para métricas de fecha
def datetime_from_utc_to_local_2(utc_datetime):
        now_timestamp = time.time()
        offset = datetime.fromtimestamp(now_timestamp) - datetime.utcfromtimestamp(
            now_timestamp
        )
        result = utc_datetime + offset
        return result.strftime("%H:%M:%S")

# Clase para definir métricas del Storage
class UtteranceLog(StoreItem):
    """
    Class for storing a log of utterances (text of messages) as a list.
    """

    def __init__(self):
        super(UtteranceLog, self).__init__()
        self.turn_question = ''
        self.turn_answer = ''
        self.turn_date = ''
        self.turn_time = ''
        self.turn_understood = ''
        #self.turn_hostname = ''
        #self.turn_ip = ''
        

class QnABot(ActivityHandler):
    def __init__(self, config: DefaultConfig):
        self.qna_maker = QnAMaker(
            QnAMakerEndpoint(
                knowledge_base_id=config.QNA_KNOWLEDGEBASE_ID,
                endpoint_key=config.QNA_ENDPOINT_KEY,
                host=config.QNA_ENDPOINT_HOST,
            )
        )
        
        cosmos_config = CosmosDbPartitionedConfig(
        cosmos_db_endpoint=config.COSMOS_DB_URI,
        auth_key=config.COSMOS_DB_PRIMARY_KEY,
        database_id=config.COSMOS_DB_DATABASE_ID,
        container_id=config.COSMOS_DB_CONTAINER_ID,
        compatibility_mode = False
        )
        self.storage = CosmosDbPartitionedStorage(cosmos_config)

    async def on_members_added_activity(
        self, members_added: [ChannelAccount], turn_context: TurnContext
    ):
        for member in members_added:
            if member.id != turn_context.activity.recipient.id:
                await turn_context.send_activity(
                    "Bienvenido al ChatBot del FNA, soy un bot a tu servicio."
                )

    async def on_message_activity(self, turn_context: TurnContext):
        utterance_log = UtteranceLog()
        usuario = turn_context.activity.text
        
        # Creación de id aleatorio
        key = ''.join(random.choices(all_chars, k=10))
        
        # Llamada al servicio QnA Maker.
        response = await self.qna_maker.get_answers(turn_context)
        if response and len(response) > 0:
            await turn_context.send_activity(MessageFactory.text(response[0].answer))
            utterance_log.turn_answer = response[0].answer
            utterance_log.turn_understood = 'S'
        else:
            await turn_context.send_activity("Discúlpame, no entendí tu pregunta.")
            utterance_log.turn_answer = "Discúlpame, no entendí tu pregunta."
            utterance_log.turn_understood = 'N'
            
            
        #------ Asignación de métricas
        utterance_log.turn_question = usuario
        utterance_log.turn_date = datetime_from_utc_to_local(turn_context.activity.timestamp)
        utterance_log.turn_time = datetime_from_utc_to_local_2(turn_context.activity.timestamp)
        
        #------
        try:
            # Guarde el mensaje de usuario en su almacenamiento.
            changes = {str(key): utterance_log}
            await self.storage.write(changes)
            
        except Exception as exception:
            # Mensaje de error.
            await turn_context.send_activity("Sorry, something went wrong storing your message!")