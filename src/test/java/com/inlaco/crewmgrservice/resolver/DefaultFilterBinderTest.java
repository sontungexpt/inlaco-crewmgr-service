// package com.inlaco.crewmgrservice.resolver;

// import static org.junit.jupiter.api.Assertions.*;

// import com.inlaco.crewmgrservice.infrastructure.web.resolver.filter.binder.DefaultFilterBinder;
// import java.util.*;
// import org.junit.jupiter.api.BeforeEach;
// import org.junit.jupiter.api.Test;
// import org.springframework.core.convert.support.DefaultConversionService;

// class DefaultFilterBinderTest {

//   private DefaultFilterBinder binder;

//   @BeforeEach
//   void setup() {
//     binder = new DefaultFilterBinder(new DefaultConversionService());
//   }

//   // ============================================================
//   // ======================= SIMPLE ===============================
//   // ============================================================

//   static class SimpleDto {
//     private String name;
//     private Integer age;

//     public void setName(String name) {
//       this.name = name;
//     }

//     public void setAge(Integer age) {
//       this.age = age;
//     }
//   }

//   @Test
//   void should_bind_simple_fields() {
//     Map<String, String[]> params =
//         Map.of(
//             "name", new String[] {"John"},
//             "age", new String[] {"25"});

//     SimpleDto dto = (SimpleDto) binder.bind(SimpleDto.class, "", params);

//     assertNotNull(dto);
//     assertEquals("John", dto.name);
//     assertEquals(25, dto.age);
//   }

//   // ============================================================
//   // ======================= COLLECTION ==========================
//   // ============================================================

//   static class CollectionDto {
//     private List<Integer> numbers;

//     public void setNumbers(List<Integer> numbers) {
//       this.numbers = numbers;
//     }
//   }

//   @Test
//   void should_bind_list_of_simple() {
//     Map<String, String[]> params = Map.of("numbers", new String[] {"1", "2", "3"});

//     CollectionDto dto = (CollectionDto) binder.bind(CollectionDto.class, "", params);

//     assertNotNull(dto);
//     assertEquals(List.of(1, 2, 3), dto.numbers);
//   }

//   // ============================================================
//   // ======================= NESTED ==============================
//   // ============================================================

//   static class Address {
//     private String city;

//     public void setCity(String city) {
//       this.city = city;
//     }
//   }

//   static class User {
//     private Address address;

//     public void setAddress(Address address) {
//       this.address = address;
//     }
//   }

//   @Test
//   void should_bind_nested_object() {
//     Map<String, String[]> params = Map.of("address.city", new String[] {"Hanoi"});

//     User user = (User) binder.bind(User.class, "", params);

//     assertNotNull(user);
//     assertNotNull(user.address);
//     assertEquals("Hanoi", user.address.city);
//   }

//   // ============================================================
//   // ======================= MAP ==============================
//   // ============================================================

//   static class MapDto {
//     private Map<String, Integer> scores;

//     public void setScores(Map<String, Integer> scores) {
//       this.scores = scores;
//     }
//   }

//   @Test
//   void should_bind_map_simple() {
//     Map<String, String[]> params =
//         Map.of(
//             "scores.math", new String[] {"10"},
//             "scores.english", new String[] {"8"});

//     MapDto dto = (MapDto) binder.bind(MapDto.class, "", params);

//     assertNotNull(dto);
//     assertEquals(10, dto.scores.get("math"));
//     assertEquals(8, dto.scores.get("english"));
//   }

//   // ============================================================
//   // ================= MAP<String, Map<String,T>> ===============
//   // ============================================================

//   static class NestedMapDto {
//     private Map<String, Map<String, Integer>> matrix;

//     public void setMatrix(Map<String, Map<String, Integer>> matrix) {
//       this.matrix = matrix;
//     }
//   }

//   @Test
//   void should_bind_nested_map() {
//     Map<String, String[]> params =
//         Map.of(
//             "matrix.row1.col1", new String[] {"1"},
//             "matrix.row1.col2", new String[] {"2"},
//             "matrix.row2.col1", new String[] {"3"});

//     NestedMapDto dto = (NestedMapDto) binder.bind(NestedMapDto.class, "", params);

//     assertNotNull(dto);
//     assertEquals(1, dto.matrix.get("row1").get("col1"));
//     assertEquals(2, dto.matrix.get("row1").get("col2"));
//     assertEquals(3, dto.matrix.get("row2").get("col1"));
//   }

//   // ============================================================
//   // ================= LIST<LIST<T>> ============================
//   // ============================================================

//   static class DeepListDto {
//     private List<List<Integer>> grid;

//     public void setGrid(List<List<Integer>> grid) {
//       this.grid = grid;
//     }
//   }

//   @Test
//   void should_bind_list_of_list() {
//     Map<String, String[]> params = Map.of("grid", new String[] {"1,2", "3,4"});

//     DeepListDto dto = (DeepListDto) binder.bind(DeepListDto.class, "", params);

//     assertNotNull(dto);
//     assertNotNull(dto.grid);
//   }

//   // ============================================================
//   // ======================= RECORD ==============================
//   // ============================================================

//   record RecordDto(String name, Integer age) {}

//   @Test
//   void should_bind_record() {
//     Map<String, String[]> params =
//         Map.of(
//             "name", new String[] {"Alice"},
//             "age", new String[] {"30"});

//     RecordDto dto = (RecordDto) binder.bind(RecordDto.class, "", params);

//     assertNotNull(dto);
//     assertEquals("Alice", dto.name());
//     assertEquals(30, dto.age());
//   }

//   // ============================================================
//   // ======================= EMPTY ===============================
//   // ============================================================

//   @Test
//   void should_return_null_if_no_params() {
//     Map<String, String[]> params = Map.of();

//     SimpleDto dto = (SimpleDto) binder.bind(SimpleDto.class, "", params);

//     assertNull(dto);
//   }

//   // ============================================================
//   // ================= CIRCULAR PROTECTION ======================
//   // ============================================================

//   static class Circular {
//     private Circular self;

//     public void setSelf(Circular self) {
//       this.self = self;
//     }
//   }

//   @Test
//   void should_not_stack_overflow_on_circular() {
//     Map<String, String[]> params = Map.of("self.self.self.self.self", new String[] {"x"});

//     Circular result = (Circular) binder.bind(Circular.class, "", params);

//     assertNotNull(result);
//   }
// }
