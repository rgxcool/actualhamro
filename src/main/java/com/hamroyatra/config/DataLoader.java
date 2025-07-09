package com.hamroyatra.config;

import com.hamroyatra.model.Destination;
import com.hamroyatra.model.TourPackage;
import com.hamroyatra.model.User;
import com.hamroyatra.repository.DestinationRepository;
import com.hamroyatra.repository.TourPackageRepository;
import com.hamroyatra.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.math.BigDecimal;

@Component
public class DataLoader implements CommandLineRunner {

    private final DestinationRepository destinationRepository;
    private final TourPackageRepository tourPackageRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    
    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    public DataLoader(DestinationRepository destinationRepository,
                     TourPackageRepository tourPackageRepository,
                     UserRepository userRepository,
                     PasswordEncoder passwordEncoder) {
        this.destinationRepository = destinationRepository;
        this.tourPackageRepository = tourPackageRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        try {
            // Wait a bit to ensure tables are created by schema.sql
            Thread.sleep(2000);
            
            // Create admin user if not exists
            try {
                if (userRepository.count() == 0) {
                    createAdminUser();
                    System.out.println("Admin user created successfully");
                }
            } catch (Exception e) {
                System.out.println("Error creating admin user: " + e.getMessage());
                e.printStackTrace();
            }
            
            // Load destinations and tour packages
            try {
                if (destinationRepository.count() == 0) {
                    loadDestinations();
                    System.out.println("Destinations and tour packages loaded successfully");
                }
            } catch (Exception e) {
                System.out.println("Error loading destinations: " + e.getMessage());
                e.printStackTrace();
            }
        } catch (Exception e) {
            System.out.println("Error during data initialization: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void createAdminUser() {
        User admin = new User();
        admin.setUsername("admin");
        admin.setPassword(passwordEncoder.encode("admin123"));
        admin.setEmail("admin@hamroyatra.com");
        admin.setFullName("System Administrator");
        admin.addRole("ADMIN");
        userRepository.save(admin);

        // Create a regular user for testing
        User user = new User();
        user.setUsername("user");
        user.setPassword(passwordEncoder.encode("user123"));
        user.setEmail("user@example.com");
        user.setFullName("Test User");
        user.addRole("USER");
        userRepository.save(user);
    }

    private void loadDestinations() {
        // Create Kathmandu destination
        Destination kathmandu = new Destination();
        kathmandu.setName("Kathmandu");
        kathmandu.setDescription("The cultural heart of Nepal");
        kathmandu.setImageUrl("/images/kathmandu.jpg");
        kathmandu.setLocation("Central Nepal");
        kathmandu.setHighlights("Durbar Square, Swayambhunath, Pashupatinath, Boudhanath");
        destinationRepository.save(kathmandu);

        // Create Pokhara destination
        Destination pokhara = new Destination();
        pokhara.setName("Pokhara");
        pokhara.setDescription("City of lakes and mountains");
        pokhara.setImageUrl("/images/pokhara.jpg");
        pokhara.setLocation("Western Nepal");
        pokhara.setHighlights("Phewa Lake, World Peace Pagoda, Sarangkot, Davis Falls");
        destinationRepository.save(pokhara);

        // Create Chitwan destination
        Destination chitwan = new Destination();
        chitwan.setName("Chitwan");
        chitwan.setDescription("Wildlife and nature");
        chitwan.setImageUrl("/images/chitwan.jpg");
        chitwan.setLocation("Southern Nepal");
        chitwan.setHighlights("Chitwan National Park, Elephant Safari, Jungle Walk, Tharu Cultural Program");
        destinationRepository.save(chitwan);

        // Create tour packages for Kathmandu
        TourPackage kathmanduExplorer = new TourPackage();
        kathmanduExplorer.setName("Kathmandu Explorer");
        kathmanduExplorer.setDescription("Explore the cultural heritage of Kathmandu Valley");
        kathmanduExplorer.setDuration(3);
        kathmanduExplorer.setPrice(new BigDecimal("250.00"));
        kathmanduExplorer.setImageUrl("/images/kathmandu-tour.jpg");
        kathmanduExplorer.setItinerary("Day 1: Kathmandu Durbar Square\nDay 2: Swayambhunath and Pashupatinath\nDay 3: Boudhanath and Bhaktapur");
        kathmanduExplorer.setInclusions("Hotel accommodation, Breakfast, Transportation, Guide");
        kathmanduExplorer.setExclusions("Lunch, Dinner, Personal expenses, Tips");
        kathmanduExplorer.setDestination(kathmandu);
        tourPackageRepository.save(kathmanduExplorer);

        // Create tour packages for Pokhara
        TourPackage pokharaRelax = new TourPackage();
        pokharaRelax.setName("Pokhara Relaxation");
        pokharaRelax.setDescription("Relax and enjoy the natural beauty of Pokhara");
        pokharaRelax.setDuration(4);
        pokharaRelax.setPrice(new BigDecimal("350.00"));
        pokharaRelax.setImageUrl("/images/pokhara-tour.jpg");
        pokharaRelax.setItinerary("Day 1: Arrival and Phewa Lake\nDay 2: Sarangkot Sunrise\nDay 3: World Peace Pagoda\nDay 4: Davis Falls and Gupteshwor Cave");
        pokharaRelax.setInclusions("Hotel accommodation, Breakfast, Transportation, Guide");
        pokharaRelax.setExclusions("Lunch, Dinner, Personal expenses, Tips");
        pokharaRelax.setDestination(pokhara);
        tourPackageRepository.save(pokharaRelax);

        // Create tour packages for Chitwan
        TourPackage chitwanSafari = new TourPackage();
        chitwanSafari.setName("Chitwan Safari");
        chitwanSafari.setDescription("Experience wildlife in Chitwan National Park");
        chitwanSafari.setDuration(3);
        chitwanSafari.setPrice(new BigDecimal("300.00"));
        chitwanSafari.setImageUrl("/images/chitwan-tour.jpg");
        chitwanSafari.setItinerary("Day 1: Arrival and Tharu Cultural Program\nDay 2: Elephant Safari and Jungle Walk\nDay 3: Canoe Ride and Bird Watching");
        chitwanSafari.setInclusions("Resort accommodation, All meals, Transportation, Guide, Safari activities");
        chitwanSafari.setExclusions("Personal expenses, Tips, Alcoholic beverages");
        chitwanSafari.setDestination(chitwan);
        tourPackageRepository.save(chitwanSafari);
    }
}
