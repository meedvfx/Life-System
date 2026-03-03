/* ============================================================
   DASHBOARD — dashboard.js
   Animated KPI counters, progress bar fills, localStorage metrics.
   ============================================================ */

(function () {
    'use strict';

    /* ---------- Animated Counter ---------- */
    function animateCounters() {
        var counters = document.querySelectorAll('[data-count]');
        if (!counters.length) return;

        var observer = new IntersectionObserver(function (entries) {
            entries.forEach(function (entry) {
                if (entry.isIntersecting) {
                    var el = entry.target;
                    var target = parseInt(el.getAttribute('data-count'), 10);
                    var suffix = el.getAttribute('data-suffix') || '';
                    var duration = 1500;
                    var start = 0;
                    var startTime = null;

                    function step(timestamp) {
                        if (!startTime) startTime = timestamp;
                        var progress = Math.min((timestamp - startTime) / duration, 1);
                        // Ease-out quad
                        var eased = 1 - (1 - progress) * (1 - progress);
                        var current = Math.floor(eased * target);
                        el.textContent = current + suffix;
                        if (progress < 1) {
                            requestAnimationFrame(step);
                        } else {
                            el.textContent = target + suffix;
                        }
                    }

                    requestAnimationFrame(step);
                    observer.unobserve(el);
                }
            });
        }, { threshold: 0.3 });

        counters.forEach(function (el) {
            observer.observe(el);
        });
    }

    /* ---------- Progress Bar Fill Animation ---------- */
    function animateProgressBars() {
        var bars = document.querySelectorAll('.progress__fill[data-width]');
        if (!bars.length) return;

        var observer = new IntersectionObserver(function (entries) {
            entries.forEach(function (entry) {
                if (entry.isIntersecting) {
                    var bar = entry.target;
                    var w = bar.getAttribute('data-width');
                    // Small delay so browser can paint
                    setTimeout(function () {
                        bar.style.width = w + '%';
                    }, 200);
                    observer.unobserve(bar);
                }
            });
        }, { threshold: 0.2 });

        bars.forEach(function (bar) {
            observer.observe(bar);
        });
    }

    /* ---------- localStorage Metrics (Optional) ---------- */
    var METRICS_KEY = 'zahzouh-metrics';

    function getMetrics() {
        try {
            var data = JSON.parse(localStorage.getItem(METRICS_KEY));
            return data || getDefaultMetrics();
        } catch (_) {
            return getDefaultMetrics();
        }
    }

    function getDefaultMetrics() {
        return {
            deepWorkHours: 142,
            completedTasks: 87,
            focusScore: 92,
            streak: 14
        };
    }

    function saveMetrics(metrics) {
        localStorage.setItem(METRICS_KEY, JSON.stringify(metrics));
    }

    function applyMetrics() {
        var metrics = getMetrics();
        // Map metric keys to data-metric attributes
        Object.keys(metrics).forEach(function (key) {
            var el = document.querySelector('[data-metric="' + key + '"]');
            if (el) {
                el.setAttribute('data-count', metrics[key]);
            }
        });
    }

    /* ---------- Project Filter (for projects page) ---------- */
    function initProjectFilter() {
        var filterBtns = document.querySelectorAll('.filter-btn[data-filter]');
        var cards = document.querySelectorAll('.project-card[data-status]');

        if (!filterBtns.length || !cards.length) return;

        filterBtns.forEach(function (btn) {
            btn.addEventListener('click', function () {
                // Toggle active state
                filterBtns.forEach(function (b) { b.classList.remove('active'); });
                btn.classList.add('active');

                var filter = btn.getAttribute('data-filter');

                cards.forEach(function (card) {
                    if (filter === 'all' || card.getAttribute('data-status') === filter) {
                        card.style.display = '';
                        // Re-trigger reveal animation
                        card.classList.remove('visible');
                        requestAnimationFrame(function () {
                            card.classList.add('visible');
                        });
                    } else {
                        card.style.display = 'none';
                    }
                });
            });
        });
    }

    /* ---------- Init ---------- */
    document.addEventListener('DOMContentLoaded', function () {
        applyMetrics();
        animateCounters();
        animateProgressBars();
        initProjectFilter();
    });

    /* Expose for optional external use */
    window.Dashboard = {
        getMetrics: getMetrics,
        saveMetrics: saveMetrics,
        animateCounters: animateCounters
    };
})();
