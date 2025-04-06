from models import *
from SQLcom import *
import matplotlib.pyplot as plt



def plot_efficiency(distances: dict[int, float]):
    plt.bar(distances.keys(), distances.values())
    plt.xlabel("Объект (name_obj)")
    plt.ylabel("Длина пути")
    plt.title("Сравнение эффективности путей")
    plt.show()

# Предположим, у нас такие результаты:
distances = calculate_object_path_distances(get_session())

# Оцениваем эффективность
efficiency = evaluate_path_efficiency(distances)

print("Метрики эффективности:")
print(f"- Относительная разница: {efficiency['relative_difference']:.2f}")
print(f"- Коэффициент вариации (CV): {efficiency['coefficient_of_variation']:.2f}")
print(f"- Оценка: {efficiency['efficiency_rating']}")

# Пример:
plot_efficiency(distances)