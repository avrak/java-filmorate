package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dal.*;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.*;
import ru.yandex.practicum.filmorate.exception.ParameterNotValidException;

import java.util.*;

@Service
@RequiredArgsConstructor
public class FilmService  {

    private final FilmRepository filmRepository;
    private final UserRepository userRepository;
    private final MpaRepository mpaRepository;
    private final FilmLikeRepository filmLikeRepository;
    private final GenresRepository genresRepository;
    private final LikeRepository likesRepository;
    private final FilmGenreRepository filmGenreRepository;

    private Set<FilmLike> getFilmLikes(Long filmId) {
        return new HashSet<>(filmLikeRepository.findByFilmId(filmId));
    }

    private Set<Genre> getFilmGenres(Long filmId) {
        return new HashSet<>(genresRepository.findByFilmId(filmId));
    }

    private Map<Long, Set<FilmLike>> getAllLikes(List<Like> likeList) {
        Map<Long, Set<FilmLike>> likes = new HashMap<>();

        for (Like like : likeList) {
            likes.computeIfAbsent(like.getFilmId(), k -> new HashSet<>());

            Set<FilmLike> filmLikeSet = likes.get(like.getFilmId());
            FilmLike filmLike = new FilmLike();
            filmLike.setUserId(like.getUserId());
            filmLikeSet.add(filmLike);
        }

        return likes;
    }

    private Map<Long, Set<Genre>> getAllGenres(List<FilmGenre> genreList) {
        Map<Long, Set<Genre>> genres = new HashMap<>();

        for (FilmGenre genre : genreList) {
            genres.computeIfAbsent(genre.getFilmId(), k -> new HashSet<>());

            Set<Genre> genreSet = genres.get(genre.getFilmId());
            Genre filmGenre = new Genre();
            filmGenre.setId(genre.getGenreId());
            filmGenre.setName(genre.getName());
            genreSet.add(filmGenre);
        }

        return genres;
    }


    public List<Film> getFilms() {
        List<Like> likeList = likesRepository.getAll();
        List<FilmGenre> genreList = filmGenreRepository.getAll();
        List<Film> filmList = filmRepository.findAll();

        filmList.forEach(film -> {
                    film.setLikes(getAllLikes(likeList).get(film.getId()));
                    film.setGenres(getAllGenres(genreList).get(film.getId()));
                }
        );

        return filmRepository.findAll();
    }

    public Film getFilmById(Long filmId) {
        Optional<Film> film = filmRepository.findById(filmId);
        if (film.isEmpty()) {
            throw new NotFoundException("Фильм с id " + filmId + " не найден.");
        }

        Film filmById = film.get();

        filmById.setLikes(getFilmLikes(filmId));
        filmById.setGenres(getFilmGenres(filmId));

        return filmById;
    }

    private void saveFilmLikes(Film film) {
        if (film.getLikes() == null || film.getLikes().isEmpty()) return;

        filmLikeRepository.deleteByFilmId(film.getId());

        for (FilmLike filmLike : film.getLikes()) {
            filmLikeRepository.save(film.getId(), filmLike.getUserId());
        }
    }

    private void saveFilmGenres(Film film) {
        if (film.getGenres() == null || film.getGenres().isEmpty()) return;

        genresRepository.deleteByFilmId(film.getId());

        for (Genre genre : film.getGenres()) {
            genresRepository.saveFilmGenre(film.getId(), genre.getId());
        }
    }

    public Film create(Film film) {
        if (mpaRepository.findById(film.getMpa().getId()).isEmpty()) {
            throw new NotFoundException("Рейтинг с id " + film.getMpa().getId() + " не найден.");
        }

        if (film.getGenres() != null && !film.getGenres().isEmpty()) {
            StringBuilder genresIdListRow = new StringBuilder();

            for (Genre genre : film.getGenres()) {
                if (!genresIdListRow.isEmpty()) {
                    genresIdListRow.append(",");
                }
                genresIdListRow.append(genre.getId());
            }

            Map<Integer, Genre> existingGenresId = new HashMap<>();
            genresRepository.findByListId(genresIdListRow.toString())
                    .forEach(genre -> existingGenresId.put(genre.getId(), genre));

            for (Genre genre : film.getGenres()) {
                if (existingGenresId.get(genre.getId()) == null) {
                    throw new NotFoundException("Жанр с id " + genre.getId() + " не найден.");
                }
            }
        }

        Film newFilm = filmRepository.save(film);

        saveFilmLikes(newFilm);
        saveFilmGenres(newFilm);

        return newFilm;
    }

    public Film update(Film newFilm) {
        if (newFilm.getId() == null) {
            throw new ParameterNotValidException("Id должен быть указан");
        }

        Film oldFilm = getFilmById(newFilm.getId());

        if (!newFilm.getName().isEmpty()) {
            oldFilm.setName(newFilm.getName());
        }

        if (!newFilm.getDescription().isEmpty()) {
            oldFilm.setDescription(newFilm.getDescription());
        }

        if (newFilm.getReleaseDate() != null) {
            oldFilm.setReleaseDate(newFilm.getReleaseDate());
        }

        if (newFilm.getMpa() != null) {
            oldFilm.setMpa(newFilm.getMpa());
        }

        filmRepository.update(oldFilm);
        saveFilmLikes(oldFilm);
        saveFilmGenres(oldFilm);


        return oldFilm;
    }

    public void setLikeByUserId(Long filmId, Long userId) {
        if (getFilmById(filmId) == null) {
            throw new NotFoundException("Фильм с id = " + filmId + " не найден");
        }

        if (userRepository.findById(userId).isEmpty()) {
            throw new NotFoundException("Пользователь с id = " + filmId + " не найден");
        }

        filmLikeRepository.save(filmId, userId);
    }

    public void deleteLikeByUserId(Long filmId, Long userId) {

        if (getFilmById(filmId) == null) {
            throw new NotFoundException("Фильм с id = " + filmId + " не найден");
        }

        if (userRepository.findById(userId).isEmpty()) {
            throw new NotFoundException("Пользователь с id = " + filmId + " не найден");
        }

        filmLikeRepository.deleteByFilmIdUserId(filmId, userId);
    }

    public Collection<Film> getPopularFilms(Long count) {
        Collection<Film> filmCollection = filmRepository.getPopularFilms(count);

        filmCollection.forEach(film -> {
                    film.setLikes(getFilmLikes(film.getId()));
                    film.setGenres(getFilmGenres(film.getId()));
                }
        );

        return filmCollection;
    }
}
