/**
 * Array based storage for Resumes
 */
public class ArrayStorage {
    Resume[] storage = new Resume[10000];

    void clear() {
        int check = 1;
        for (int i = 0; i < storage.length; i++) {
            if (storage[i] == null) {
                return;
            }
            storage[i] = null;
        }
    }

    void save(Resume r) {
        for (int i = 0; i < storage.length; i++) {
            if (storage[i] != null && storage[i].toString().equals(r.toString())) {
                System.out.println("Такой элемент уже существует");
                return;
            }
            if (storage[i] == null) {
                storage[i] = r;
                return;
            }
        }
    }

    Resume get(String uuid) {
        for (Resume resume : storage) {
            if (resume != null && resume.toString().equals(uuid)) {
                return resume;
            }
        }
        return null;
    }

    void delete(String uuid) {
        int index = 0;
        for (int i = 0; i < storage.length; i++) {
            if (storage[i] == null) {
                return;
            }
            if (storage[i].toString().equals(uuid)) {
                storage[i] = null;
                index = i + 1;
                break;
            }
        }
        if (storage[index] != null) {
            for (int i = index; i < storage.length; i++) {
                if (storage[i] == null) {
                    storage[i - 1] = null;
                    return;
                }
                storage[i - 1] = storage[i];
            }
        }
    }

    /**
     * @return array, contains only Resumes in storage (without null)
     */
    Resume[] getAll() {
        int len = 0;
        for (Resume resume : storage) {
            if (resume == null) {
                break;
            }
            len++;
        }
        Resume[] copyArray = new Resume[len];
        System.arraycopy(storage, 0, copyArray, 0, len);
        return copyArray;
    }

    int size() {
        int size = 0;
        for (Resume resume : storage) {
            if (resume == null) {
                break;
            }
            size++;
        }
        return size;
    }
}
