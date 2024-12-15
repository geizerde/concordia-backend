# print("12170 1.0 \n 10991 0.8 \n24767 0.7")

import random
import sys

import numpy as np
import pandas as pd
from sklearn.neighbors import NearestNeighbors
from sklearn.preprocessing import StandardScaler


class UserProfile:
    def __init__(self, dataset_path):
        self.df = pd.read_csv(dataset_path)
        self.df = self.df.dropna()  # Удаление пропусков
        self._preprocess_data()

    def _preprocess_data(self):
        scaler = StandardScaler()
        self.df[['age']] = scaler.fit_transform(self.df[['age']])
        # Разделяем интересы и создаем бинарные столбцы
        interests_df = self.df['interests'].str.get_dummies(sep=', ')
        self.df = pd.concat([self.df.drop('interests', axis=1), interests_df], axis=1)

    def find_similar_users(self, user_index, count_neighbors=100, features=None):
        # Если не указаны параметры, используем все, кроме 'ID'
        if features is None:
            features = self.df.columns.tolist()
            features.remove('ID')  # Исключаем ID из признаков

        # Применение KNN для нахождения ближайших соседей
        knn = NearestNeighbors(n_neighbors=count_neighbors, metric='cosine')
        knn.fit(self.df[features])  # Используем только выбранные признаки

        distances, indices = knn.kneighbors(self.df.loc[[user_index], features])

        # Получаем данные для всех похожих пользователей, включая пользователя с мутациями
        similar_users = self.df.iloc[indices[0]]  # Включаем пользователя с индексом 0

        # Нормализуем расстояния
        #d_min = np.min(distances)
        #d_max = np.max(distances)
        #normalized_distances = np.round(1 - (distances - d_min) / (d_max - d_min), 1)

        normalized_distances = np.round(1-distances, 1)
        return indices[0], similar_users, normalized_distances[0]  # Возвращаем всех похожих, включая самого себя

    def get_user_index_by_id(self, user_id):
        """Получить индекс пользователя по его ID."""
        return self.df[self.df['ID'] == user_id].index[0]

    def randomize_user_features(self, user_index, mutation_chance=0.1):
        features = self.df.columns.tolist()
        # Получаем текущие значения признаков пользователя
        user_features = self.df.loc[user_index, features].copy()

        # Проходим по всем признакам
        for feature in features:
            if random.random() < mutation_chance:  # Сравниваем с вероятностью мутации
                if user_features[feature].dtype == 'bool':
                    # Меняем 1 на 0 и наоборот для булевых значений
                    user_features[feature] = not user_features[feature]
                elif user_features[feature].dtype in ['int', 'float']:
                    # Приводим к int или float, если это числовые значения
                    user_features[feature] = 1 - user_features[feature]  # Пример изменения
        """
        print(user_features)"""
        self.df.loc[user_index, features] = user_features


def start():


    # Получаем параметры из конфигурации
    user_id = int(sys.argv[1]) # Преобразуем в int, если ключ существует
    count_neighbors = int(sys.argv[2]) # Преобразуем в int
    mutation_chance = float(sys.argv[3]) # Преобразуем в float
    path_to_dataset = sys.argv[4]

    print(user_id)
    print(count_neighbors)
    print(mutation_chance)
    print(path_to_dataset)
    #print(user_id, count_neighbors, mutation_chance)
    # Доступ к переменным окружения
#     path_to_dataset = "C:\\Users\\geize\\Desktop\\Project\\concordia-backend\\src\\main\\java\\ru\\sirius\\concordia\\match\\ml\\KNN\\output.csv"

    print(path_to_dataset)

    user_profile = UserProfile(path_to_dataset)
    user_index = user_profile.get_user_index_by_id(user_id)
    user_profile.randomize_user_features(user_index=user_index, mutation_chance=mutation_chance)
    similar_indices, similar_users, normalized_distances = user_profile.find_similar_users(user_index, count_neighbors)

    # Удаляем текущего пользователя из списка похожих
    similar_indices = [idx for idx in similar_indices if idx != user_index]
    for idx, d in zip(similar_indices, normalized_distances):
        print(f"{user_profile.df.loc[idx, 'ID']} {d}")

    """
    print(f"Похожие пользователи для пользователя {user_index}: {similar_indices}")
    print("Информация о пользователе и его похожих:")
    print(similar_users)
    # Расстояния для каждого из ближайших соседей
    for i, distance in zip(similar_indices, normalized_distances):
        print(f"Пользователь {i} имеет схожесть {distance}")
        print(user_profile.df.iloc[i])  # Вывод данных для каждого из ближайших соседей

    print(similar_indices)
    """
start()
