package com.cs.uwindsor.group.project;

import java.io.Serializable;
import java.util.List;

// Basic movie class, holds movie properties
// All public because im lazy...
public class Movie implements Serializable {

	private static final long serialVersionUID = -2164990576839617574L;

	public String thumb = "";
	public String poster = "http://hwcdn.themoviedb.org/posters/01e/4bc97392017a3c57fe03501e/when-the-sun-goes-down-cover.jpg";

	public static class Posters implements Serializable {

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		public void setImage (Image i) {

			_image = i;

		};

		public Image getImage () {

			return _image;
		}

		private Image _image;

		public static class Image implements Serializable {

			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;
			private String _type;
			private String _size;
			private int _height;
			private int _width;
			private String _url;

			public String getType () {

				return _type;
			}

			public String getSize () {

				return _size;
			}

			public int getHeight () {

				return _height;
			}

			public int getWidth () {

				return _width;
			}

			public String getUrl () {

				return _url;
			}

			public void setType (String type) {

				_type = type;
			}

			public void setSize (String size) {

				_size = size;
			}

			public void setHeight (int height) {

				_height = height;
			}

			public void setWidth (int width) {

				_width = width;
			}

			public void setUrl (String url) {

				_url = url;
			}

			public void setId (String id) {

			};
		}
	}

	public static class Backdrops implements Serializable {

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		public void setImage (Image i) {

		};

		public static class Image implements Serializable {

			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;
			private String _type;
			private String _size;
			private int _height;
			private int _width;
			private String _url;

			public String getType () {

				return _type;
			}

			public String getSize () {

				return _size;
			}

			public int getHeight () {

				return _height;
			}

			public int getWidth () {

				return _width;
			}

			public String getUrl () {

				return _url;
			}

			public void setType (String type) {

				_type = type;
			}

			public void setSize (String size) {

				_size = size;
			}

			public void setHeight (int height) {

				_height = height;
			}

			public void setWidth (int width) {

				_width = width;
			}

			public void setUrl (String url) {

				_url = url;
			}

			public void setId (String id) {

			};
		}
	}

	public static class Genres implements Serializable {

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		public void setType (String s) {

		};

		public void setUrl (String url) {

		};

		public void setName (String name) {

		};

		public void setId (int id) {

		};
	}

	public static class Studios implements Serializable {

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		public void setUrl (String s) {

		};

		public void setName (String s) {

		};

		public void setId (int i) {

		};
	}

	public static class languages_spoken implements Serializable {

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		public void setCode (String code) {

		};

		public void setName (String name) {

		};

		public void setnative_name (String nat) {

		};
	}

	public static class Countries implements Serializable {

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		public void setCode (String code) {

		};

		public void setUrl (String s) {

		};

		public void setName (String s) {

		};
	}

	public static class Cast implements Serializable {

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		public String name;
		public String job;
		public String department;
		public String character;
		public int id;

		public void setName (String n) {

			name = n;
		};

		public void setJob (String j) {

			job = j;
		};

		public void setDepartment (String d) {

			department = d;
		};

		public void setCharacter (String c) {

			character = c;
		};

		public void setId (int i) {

			id = i;
		};

		public void setOrder (int o) {

		};

		public void setcast_id (int i) {

		};

		public void setUrl (String s) {

		};

		public void setProfile (String s) {

		};
	}

	List<Posters> _posters;

	public double score;
	public double popularity;
	public double rating;
	public String originalname;
	public String title;
	public int id;
	public String imdbid;
	public String url;
	public String overview;
	public String released;
	public String trailer;
	public Cast[] cast;

	public void setThumb (String t) {

		thumb = t;
	}

	public double getPopularity () {

		return popularity;
	};

	public String getOriginalName () {

		return originalname;
	};

	public String getName () {

		return title;
	};

	public int getId () {

		return this.id;
	};

	public String getImdb_Id () {

		return imdbid;
	};

	public String getUrl () {

		return url;
	};

	public String getOverview () {

		return overview;
	};

	public String getReleased () {

		return released;
	};

	public void setPopularity (double pop) {

		popularity = pop;
	}

	public void setName (String name) {

		title = name;
	}

	public void setScore (double score) {

		this.score = score;
	}

	public void setTranslated (boolean translated) {

	};

	public void setAdult (boolean adult) {

	};

	public void setLanguage (String lang) {

	};

	public void setoriginal_name (String name) {

		originalname = name;
	};

	public void setalternative_name (String name) {

	};

	public void setmovie_type (String type) {

	};

	public void setId (int id) {

		this.id = id;
	};

	public void setimdb_id (String id) {

		imdbid = id;
	};

	public void setUrl (String url) {

		this.url = url;
	}

	public void setVotes (int votes) {

	};

	public void setRating (double rating) {

		// Don't get rating from web.
		//this.rating = rating;
	}

	public void setCertification (String overview) {

	}

	public void setOverview (String overview) {

		this.overview = overview;
	}

	public void setReleased (String released) {

		this.released = released;
	}

	public void setRuntime (int r) {

	}

	public void setTagline (String s) {

	};

	public void setBudget (int i) {

	};

	public void setHomepage (String s) {

	};

	public void setTrailer (String s) {

		trailer = s;
	}

	public void setRevenue (int s) {

	};

	public void setStatus (String s) {

	};

	public void setKeywords (List<String> s) {

	};

	public void setGenres (List<Genres> genre) {

	};

	public void setStudios (List<Studios> s) {

	};

	public void setlanguages_spoken (List<languages_spoken> s) {

	};

	public void setCountries (List<Countries> c) {

	};

	public void setCast (Cast[] c) {

		cast = c;
	}

	public void setPosters (List<Posters> poster) {

		for (Posters p : poster) {
			if (p.getImage().getSize().equals("cover")) {
				this.thumb = p.getImage().getUrl();
			}
		}
		if (this.thumb.equals("")) {
			for (Posters p : poster) {
				if (p.getImage().getSize().equals("thumb")) {
					this.thumb = p.getImage().getUrl();
				}
			}
		}
		_posters = poster;
	}

	public void setBackdrops (List<Backdrops> poster) {

	}

	public void setVersion (int ver) {

	}

	public void setlast_modified_at (String a) {

	}
}
