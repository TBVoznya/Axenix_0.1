from sqlalchemy import create_engine, Column, String
from sqlalchemy.exc import SQLAlchemyError
from sqlalchemy.orm import declarative_base, sessionmaker
from models import ProductType, Product, ObjLocation, ObjContent, Len
from collections import defaultdict
from math import sqrt
from statistics import mean, stdev


# Настройки подключения к базе данных PostgreSQL
host = "192.168.161.9"
database = "AxShop"
user = "postgres"
password = "mypassword"
port = "5435"

# Создание строки подключения к базе данных
engine = create_engine(f"postgresql://{user}:{password}@{host}:{port}/{database}",
                       client_encoding="utf-8",
                       connect_args={"options": "-c search_path=public"})



# Создание сессии для взаимодействия с базой данных
Session = sessionmaker(bind=engine)

# Функция для получения сессии
def get_session():
    return Session()


def calculate_object_path_distances(db_session: Session) -> dict[int, float]:
    # Загружаем все точки, упорядоченные по id_content
    query = db_session.query(Len).order_by(Len.id_content)
    lens_objects = query.all()

    # Группируем точки по объектам (name_obj)
    obj_points = defaultdict(list)
    for lens in lens_objects:
        if lens.x_fin is not None and lens.y_fin is not None:
            obj_points[lens.name_obj].append((lens.x_fin, lens.y_fin))

    # Считаем расстояния для каждого объекта
    object_distances = {}

    for obj_id, points in obj_points.items():
        if not points:
            continue

        total_distance = 0.0
        prev_x, prev_y = 0.0, 0.0  # Начинаем с (0, 0)

        for x, y in points:
            dx = x - prev_x
            dy = y - prev_y
            distance = sqrt(dx ** 2 + dy ** 2)
            total_distance += distance
            prev_x, prev_y = x, y  # Переходим к следующей точке

        object_distances[obj_id] = total_distance

    return object_distances


def evaluate_path_efficiency(distances: dict[int, float]) -> dict:
    """
    Анализирует эффективность путей на основе их длин.

    Args:
        distances: Словарь {name_obj: total_distance} (результат calculate_object_path_distances).

    Returns:
        Словарь с метриками эффективности:
        {
            "relative_difference": относительная разница (0-1),
            "coefficient_of_variation": коэффициент вариации (CV),
            "efficiency_rating": оценка ("high", "medium", "low")
        }
    """
    if not distances:
        return {"error": "No distances provided"}

    values = list(distances.values())
    max_dist = max(values)
    min_dist = min(values)

    # 1. Относительная разница (ваш метод)
    if max_dist == 0:
        relative_diff = 0.0
    else:
        relative_diff = (max_dist - min_dist) / max_dist

    # 2. Коэффициент вариации (дополнительная метрика)
    avg = mean(values)
    cv = (stdev(values) / avg) if avg != 0 else 0.0

    # 3. Оценка эффективности
    if relative_diff < 0.3 or cv < 0.3:
        efficiency = "high"
    elif relative_diff < 0.6 or cv < 0.6:
        efficiency = "medium"
    else:
        efficiency = "low"

    return {
        "relative_difference": relative_diff,
        "coefficient_of_variation": cv,
        "efficiency_rating": efficiency
    }
