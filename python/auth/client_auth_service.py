import hashlib
import hmac

def get_auth(scop,ts, app_id, app_secret):
    tt = (app_id + ts).encode('utf-8')
    md5 = hashlib.md5()
    md5.update(tt)
    baseString = md5.hexdigest()
    baseString = bytes(baseString, encoding='utf-8')
    apiKey = app_secret.encode('utf-8')
    signa = hmac.new(apiKey, baseString, hashlib.sha256).hexdigest()
    return "V1-HMAC-SHA256;Scope={};Credential={};Signature={}".format(scop,app_id,signa)
