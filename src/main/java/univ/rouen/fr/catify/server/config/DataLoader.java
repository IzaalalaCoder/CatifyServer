package univ.rouen.fr.catify.server.config;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import univ.rouen.fr.catify.server.entity.Category;
import univ.rouen.fr.catify.server.repository.CategoryRepository;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.List;

@Component
public class DataLoader implements ApplicationRunner {

    private final CategoryRepository categoryRepository;

    @Autowired
    public DataLoader(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    public void run(ApplicationArguments args) {
        insertCategories();
    }

    @Transactional
    private void insertCategories() {
        if (this.categoryRepository.count() == 0) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(Date.from(LocalDate.of(2022, 1, 1)
                    .atStartOfDay(ZoneId.systemDefault()).toInstant()));

            Map<String, String> categoriesStringMap = new HashMap<>();
            Map<String, Category> categoriesCategoryMap = new HashMap<>();
            this.initializeData(categoriesStringMap);

            for (String key : categoriesStringMap.keySet()) {
                Category c = new Category(key, calendar.getTime());
                categoriesCategoryMap.put(key, c);
                calendar.add(Calendar.HOUR, 5);
            }

            for (Map.Entry<String, Category> categoryEntry : categoriesCategoryMap.entrySet()) {
                String parent = categoryEntry.getKey();
                Category c = categoryEntry.getValue();
                String parentCategoryString = categoriesStringMap.get(parent);

                if (parentCategoryString != null) {
                    Category parentCategoryCategory = categoriesCategoryMap.get(parentCategoryString);
                    c.setParent(parentCategoryCategory);
                }
            }

            List<Category> allCategories = categoriesCategoryMap.values().stream().toList();
            categoryRepository.saveAll(allCategories);
        }
    }

    private void initializeData(Map<String, String> categoriesMap) {
        categoriesMap.put("Animaux", null);
        categoriesMap.put("Mammifères", "Animaux");
        categoriesMap.put("Carnivores", "Mammifères");
        categoriesMap.put("Félins", "Carnivores");
        categoriesMap.put("Lion", "Félins");
        categoriesMap.put("Tigre", "Félins");
        categoriesMap.put("Léopard", "Félins");
        categoriesMap.put("Chats", "Félins");
        categoriesMap.put("Canidés", "Carnivores");
        categoriesMap.put("Chiens", "Canidés");
        categoriesMap.put("Malinois", "Chiens");
        categoriesMap.put("Loup", "Canidés");
        categoriesMap.put("Renard", "Canidés");
        categoriesMap.put("Herbivores", "Mammifères");
        categoriesMap.put("Chevaux", "Herbivores");
        categoriesMap.put("Éléphants", "Herbivores");
        categoriesMap.put("Girafes", "Herbivores");
        categoriesMap.put("Primates", "Mammifères");
        categoriesMap.put("Singes", "Primates");
        categoriesMap.put("Gorille", "Singes");
        categoriesMap.put("Chimpanzé", "Singes");
        categoriesMap.put("Orang-outan", "Singes");
        categoriesMap.put("Humains", "Primates");
        categoriesMap.put("Oiseaux", "Animaux");
        categoriesMap.put("Rapaces", "Oiseaux");
        categoriesMap.put("Aigles", "Rapaces");
        categoriesMap.put("Faucons", "Rapaces");
        categoriesMap.put("Chouettes", "Rapaces");
        categoriesMap.put("Passereaux", "Oiseaux");
        categoriesMap.put("Moineaux", "Passereaux");
        categoriesMap.put("Merles", "Passereaux");
        categoriesMap.put("Mésanges", "Passereaux");
        categoriesMap.put("Reptiles", "Animaux");
        categoriesMap.put("Serpents", "Reptiles");
        categoriesMap.put("Python", "Serpents");
        categoriesMap.put("Cobra", "Serpents");
        categoriesMap.put("Viperes", "Serpents");
        categoriesMap.put("Crocodiliens", "Reptiles");
        categoriesMap.put("Crocodiles", "Crocodiliens");
        categoriesMap.put("Alligators", "Crocodiliens");
        categoriesMap.put("Amphibiens", "Animaux");
        categoriesMap.put("Grenouilles", "Amphibiens");
        categoriesMap.put("Grenouilles vertes", "Grenouilles");
        categoriesMap.put("Crapauds", "Grenouilles");
        categoriesMap.put("Salamandres", "Amphibiens");
        categoriesMap.put("Poissons", "Animaux");
        categoriesMap.put("Poissons marins", "Poissons");
        categoriesMap.put("Requins", "Poissons marins");
        categoriesMap.put("Requin blanc", "Requins");
        categoriesMap.put("Requin tigre", "Requins");
        categoriesMap.put("Thons", "Poissons marins");
        categoriesMap.put("Poissons d'eau douce", "Poissons");
        categoriesMap.put("Truites", "Poissons d'eau douce");
        categoriesMap.put("Carpes", "Poissons d'eau douce");
        categoriesMap.put("Insectes", "Animaux");
        categoriesMap.put("Papillons", "Insectes");
        categoriesMap.put("Monarque", "Papillons");
        categoriesMap.put("Azuré", "Papillons");
        categoriesMap.put("Abeilles", "Insectes");
        categoriesMap.put("Fourmis", "Insectes");
        categoriesMap.put("Arachnides", "Animaux");
        categoriesMap.put("Araignées", "Arachnides");
        categoriesMap.put("Araignée de jardin", "Araignées");
        categoriesMap.put("Veuve noire", "Araignées");
        categoriesMap.put("Scorpions", "Arachnides");
    }
}
