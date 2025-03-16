echo "Starting the kubernetes app"

$uploaderFolder = "uploader"
$kubernetesFolder = "kubernetes"
$persistentFolder = "persistent-volumes"
$converterFolder = "converter"
$configmapFolder = "configmap"

$pvFile = "persistent-volume.yml"
$pvcFile = "persistent-volume-claim.yml"
$cfgMapFile = "configmap.yml"
$uploaderSvcDplFile = "uploader.yml"
$converterSvcDplFile = "converter.yml"

$CurrentPath = Get-Location
$K8sPath = Join-Path $CurrentPath $kubernetesFolder

$K8sPersistent = Join-Path $K8sPath $persistentFolder
$K8sConfigMap = Join-Path $K8sPath $configmapFolder
$K8sUploader = Join-Path $K8sPath $uploaderFolder
$K8sConverter = Join-Path $K8sPath $converterFolder

$PersistentVolume = Join-Path $K8sPersistent $pvFile
$PersistentVolumeClaim = Join-Path $K8sPersistent $pvcFile 
$ConfigMap = Join-Path $K8sConfigMap $cfgMapFile
$SvcDplUploader = Join-Path $K8sUploader $uploaderSvcDplFile
$SvcDplConverter = Join-Path $K8sConverter $converterSvcDplFile

kubectl apply -f $PersistentVolume
kubectl apply -f $PersistentVolumeClaim
kubectl apply -f $ConfigMap
kubectl apply -f $SvcDplUploader
kubectl apply -f $SvcDplConverter

kubectl port-forward service/uploader-service 8080:8080

 curl -Method Get "http://0.0.0.0:8080/healthcheck_global"