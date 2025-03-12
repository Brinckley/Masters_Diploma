echo "Starting the kubernetes app"

$uploader = "uploader"
$kubernetes = "kubernetes"
$persistent = "persistent-volumes"

$uploaderSvcDplFile = "uploader-service.yml"
$pvFile = "persistent-volume.yml"
$pvcFile = "persistent-volume-claim.yml"

$CurrentPath = Get-Location
$K8sPath = Join-Path $CurrentPath $kubernetes
$K8sUploader = Join-Path $K8sPath $uploader
$K8sPersistent = Join-Path $K8sPath $persistent

$SvcDplUploader = Join-Path $K8sUploader $uploaderSvcDplFile
$PersistentVolume = Join-Path $K8sPersistent $pvFile
$PersistentVolumeClaim = Join-Path $K8sPersistent $pvcFile 

kubectl apply -f $PersistentVolume
kubectl apply -f $PersistentVolumeClaim
kubectl apply -f $SvcDplUploader

kubectl port-forward service/uploader-service 8080:8080

curl -Method Get "http://your-endpoint.com/api"