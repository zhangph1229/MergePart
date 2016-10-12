package cn.edu.neu.zhangph.method;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import com.github.davidmoten.rtree.Entries;
import com.github.davidmoten.rtree.Entry;
import com.github.davidmoten.rtree.RTree;
import com.github.davidmoten.rtree.geometry.Geometries;
import com.github.davidmoten.rtree.geometry.Point;

import rx.Observable;
import rx.functions.Action1;
import rx.functions.Func0;
import rx.functions.Func1;
import rx.observables.StringObservable;


public class CreateTree {
	@SuppressWarnings("rawtypes")
	public static RTree createTree(String file){
        RTree<Object, Point> tree = RTree.star().create();
        tree = tree.add(entries(file)).last().toBlocking().single();
        System.gc();
        System.out.println("The tree size is " + tree.size());
//        tree.nearest(p, maxDistance, maxCount)
        return tree;
	}
    public static Observable<Entry<Object, Point>> entries(final String file) {
        Observable<String> source = Observable.using(new Func0<InputStream>() {

			@Override
            public InputStream call() {
                try {
                	return new FileInputStream(file);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }, new Func1<InputStream, Observable<String>>() {
            @Override
            public Observable<String> call(InputStream is) {
                return StringObservable.from(new InputStreamReader(is));
            }
        }, new Action1<InputStream>() {
            @Override
            public void call(InputStream is) {
                try {
                    is.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        return StringObservable.split(source, "\n")
                .flatMap(new Func1<String, Observable<Entry<Object, Point>>>() {

					@Override
					public Observable<Entry<Object, Point>> call(String line) {
						if (line.trim().length() > 0) {
							String[] items = line.split(",");
							int id = Integer.parseInt(items[0]);
							double lat = Double.parseDouble(items[1]);
							double lon = Double.parseDouble(items[2]);
							return Observable.just(Entries.entry(new Object(),
									Geometries.point(lat, lon, id)));
						} else
							return Observable.empty();
					}
                });
    }

//    static List<Entry<Object, Point>> entriesList() {
//        List<Entry<Object, Point>> result = entries().toList().toBlocking().single();
//        System.out.println("loaded greek earthquakes into list");
//        return result;
//    }
}
