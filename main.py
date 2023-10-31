# This is a sample Python script.
from flask import Flask, request, jsonify
import requests
import numpy as np
from sklearn.metrics.pairwise import cosine_similarity

# Press Shift+F10 to execute it or replace it with your code.
# Press Double Shift to search everywhere for classes, files, tool windows, actions, and settings.

app = Flask(__name__)


@app.route('/chat', methods=['GET', 'POST'])
def chat():
    if request.method == 'GET':
        message = request.args.get('msg')
    elif request.method == 'POST':
        message = request.json['msg']
    else:
        return jsonify({'error': 'Invalid request method'})
    openai_secret_key = "ap-WTmMmOIytml7Ko0P2ppDeMHH9zybmrntL6Z9jkJMVFKUGfY0"

    headers = {
        'Content-Type': 'application/json',
        'Authorization': f'Bearer {openai_secret_key}'
    }
    data = {
        "model": "gpt-3.5-turbo",
        "messages": [{"role": "user", "content": message}],
        "temperature": 0.2
    }

    response = requests.post('https://api.aiproxy.io/v1/chat/completions', headers=headers, json=data)
    response_data = response.json()
    text = response_data["choices"][0]

    return jsonify({'text': text})
    # Use a breakpoint in the code line below to debug your script.
    # Press Ctrl+F8 to toggle the breakpoint.


@app.route('/embedding', methods=['GET', 'POST'])
def embedding():
    if request.method == 'GET':
        message = request.args.get('msg')
    elif request.method == 'POST':
        message = request.json['msg']
    else:
        return jsonify({'error': 'Invalid request method'})
    openai_secret_key = "ap-WTmMmOIytml7Ko0P2ppDeMHH9zybmrntL6Z9jkJMVFKUGfY0"

    headers = {
        'Content-Type': 'application/json',
        'Authorization': f'Bearer {openai_secret_key}'
    }
    data = {
        "model": "text-embedding-ada-002",
        "input": message,
    }

    response = requests.post('https://api.aiproxy.io/v1/embeddings', headers=headers, json=data)
    response_data = response.json()
    embedding = response_data["data"][0]["embedding"]

    url = 'http://47.109.45.24:5254/api/lawyer/getAllLawyers'
    lawyer = requests.get(url)
    lawyer_data = lawyer.json()
    recommend_id = lawyer_data['data'][0]
    similarity_max = 0

    for ind, ly in enumerate(lawyer_data['data']):
        temp = [float(x) for x in ly['lawCode'].split()]
        embedding = np.array(embedding).reshape(1, -1)
        temp = np.array(temp).reshape(1, -1)
        similarity = cosine_similarity(embedding, temp)
        if similarity > similarity_max:
            recommend_id = lawyer_data['data'][ind]['id']
            similarity_max = similarity

    return jsonify({'recommend_id': recommend_id})


# Press the green button in the gutter to run the script.
if __name__ == '__main__':
    app.run(port=5253)

# See PyCharm help at https://www.jetbrains.com/help/pycharm/
