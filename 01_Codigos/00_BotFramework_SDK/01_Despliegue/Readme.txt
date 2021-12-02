# Indicaciones para el despliegue del sdk desarrollado en python, al servicio de Azure Bot en la nube.


1. Actualizar el archivo requirements.txt del proyecto, usar los paquetes y versiones del archivo que está en esta carpeta.

2. Registrar la aplicación, usar este comando reemplazando el nombre de la aplicación y la contraseña:
    
   ~~~
   az ad app create --display-name "nombre_de_aplicación" --password "contraseña" --available-to-other-tenants
   ~~~

3. Desplegar los Servicios de App Service Plan, Azure Bot y WebApp usando las respectivas credenciales y nombre de grupos de recursos, este comendo se debe ejecutar en la carpeta donde esta la plantilla de despligue JSON:

~~~
   az deployment group create --resource-group "Nombre de grupo de recursos" --template-file "plantilla-despliegue-bot-fna.json" --parameters appId="ID de la aplicación registrada en el paso anterior" appSecret="Contraseña registrada en el paso anterior" botId="Identificador del bot, puede ser valores alfanuméricos" newWebAppName="Nombre de la aplicación WEB" newAppServicePlanName="Nombre del service plan sobre el que se despliega la aplicación WEB"   appServicePlanLocation="East US" --name "Nombre del bot"
~~~

4. Empaquetar el proyecto en un zip, en el zip debe estar el archivo app.py y el archivo requirements.

5. Subir a la aplicación web con el comando:

  ~~~
  az webapp deployment source config-zip --resource-group "Nombre del grupo de recursos" --name "Nombre de la aplicación web" --src "nombre del archivo zip que va a publicar"
  ~~~
